package com.mlib.contexts;

import com.mlib.contexts.base.Condition;
import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;
import com.mlib.contexts.data.IEntityData;
import com.mlib.data.SerializableHelper;
import com.mlib.data.SerializableStructure;
import com.mlib.modhelper.AutoInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 BE CAREFUL!
 .
 This context is usually handled when world chunks are being loaded and most world modifications
 like adding new mobs, checking chunk/structure info etc. may cause deadlocks on these chunks.
 Use ContextSafe whenever a world interaction is required! (see EntityJoinLevelEvent for more info)
 */
@Mod.EventBusSubscriber
public class OnSpawned {
	public static final Consumer< Data > CANCEL = data->data.cancel.run();
	static final List< DataSafe > CLIENT_PENDING_LIST = new ArrayList<>();
	static final List< DataSafe > SERVER_PENDING_LIST = new ArrayList<>();

	public static Context< Data > listen( Consumer< Data > consumer ) {
		return Contexts.get( Data.class ).add( consumer );
	}

	public static Context< DataSafe > listenSafe( Consumer< DataSafe > consumer ) {
		return Contexts.get( DataSafe.class ).add( consumer );
	}

	@SubscribeEvent
	public static void onSpawn( EntityJoinLevelEvent event ) {
		if( !( event.getEntity() instanceof LivingEntity entity ) )
			return;

		Contexts.get( Data.class ).dispatch( new Data( entity, event.loadedFromDisk(), ()->event.setCanceled( true ) ) );
		List< DataSafe > list = entity.level.isClientSide ? CLIENT_PENDING_LIST : SERVER_PENDING_LIST;
		list.add( new DataSafe( entity, event.loadedFromDisk() ) );
	}

	@SubscribeEvent
	public static void onClientTickEnd( TickEvent.ClientTickEvent event ) {
		if( event.phase != TickEvent.Phase.END )
			return;

		handle( CLIENT_PENDING_LIST );
	}

	@SubscribeEvent
	public static void onServerTickEnd( TickEvent.ServerTickEvent event ) {
		if( event.phase != TickEvent.Phase.END )
			return;

		handle( SERVER_PENDING_LIST );
	}

	public static < DataType extends Data > Condition< DataType > isNotLoadedFromDisk() {
		return new Condition<>( data->!data.loadedFromDisk );
	}

	public static < DataType extends Data > Condition< DataType > is( Class< ? >... classes ) {
		return new Condition<>( data->Arrays.stream( classes ).anyMatch( clazz->data.target.getClass().equals( clazz ) ) );
	}

	private static void handle( List< DataSafe > pendingList ) {
		List< DataSafe > list;
		synchronized( pendingList ) {
			list = new ArrayList<>( pendingList );
			pendingList.clear();
		}
		list.forEach( data->{
			if( data.target.isAddedToWorld() ) {
				Contexts.get( DataSafe.class ).dispatch( data );
			}
		} );
	}

	public static class Data implements IEntityData {
		public final LivingEntity target;
		public final boolean loadedFromDisk;
		public final Runnable cancel;
		@Nullable
		private final MobSpawnType mobSpawnType;

		public Data( LivingEntity target, boolean loadedFromDisk, Runnable cancel ) {
			this.target = target;
			this.loadedFromDisk = loadedFromDisk;
			this.cancel = cancel;
			this.mobSpawnType = target instanceof Mob mob ? SerializableHelper.read( SpawnTypeInfo::new, mob.getPersistentData() ).mobSpawnType : null;
		}

		@Override
		public Entity getEntity() {
			return this.target;
		}

		@Nullable
		public final MobSpawnType getSpawnType() {
			return this.mobSpawnType;
		}
	}

	public static class DataSafe extends Data {
		public DataSafe( LivingEntity target, boolean loadedFromDisk ) {
			super( target, loadedFromDisk, ()->{} );
		}
	}

	// FIX FOR VERSIONS BEFORE 1.20 WHERE IT WAS ADDED TO MOB CLASS
	@AutoInstance
	public static class SpawnTypeUpdater {
		public SpawnTypeUpdater() {
			OnCheckSpawn.listen( data->SerializableHelper.write( ()->new SpawnTypeInfo( data.spawnType ), data.mob.getPersistentData() ) );
		}
	}

	public static class SpawnTypeInfo extends SerializableStructure {
		public MobSpawnType mobSpawnType = null;

		public SpawnTypeInfo() {
			this.defineEnum( "MajruszLibraryMobSpawnType", ()->this.mobSpawnType, x->this.mobSpawnType = x, MobSpawnType::values );
		}

		public SpawnTypeInfo( MobSpawnType mobSpawnType ) {
			this();

			this.mobSpawnType = mobSpawnType;
		}
	}
}