package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.Condition;
import com.mlib.gamemodifiers.ContextBase;
import com.mlib.gamemodifiers.Contexts;
import com.mlib.gamemodifiers.data.ILevelData;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

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
	static final List< DataSafe > CLIENT_PENDING_LIST = new ArrayList<>();
	static final List< DataSafe > SERVER_PENDING_LIST = new ArrayList<>();

	public static ContextBase< Data > listen( Consumer< Data > consumer ) {
		return Contexts.get( Data.class ).add( consumer );
	}

	public static ContextBase< DataSafe > listenSafe( Consumer< DataSafe > consumer ) {
		return Contexts.get( DataSafe.class ).add( consumer );
	}

	@SubscribeEvent
	public static void onSpawn( EntityJoinLevelEvent event ) {
		if( !( event.getEntity() instanceof LivingEntity entity ) )
			return;

		Contexts.get( Data.class ).dispatch( new Data( entity, event.loadedFromDisk() ) );
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

	public static class Data implements ILevelData {
		public final LivingEntity target;
		public final boolean loadedFromDisk;

		public Data( LivingEntity target, boolean loadedFromDisk ) {
			this.target = target;
			this.loadedFromDisk = loadedFromDisk;
		}

		@Override
		public Level getLevel() {
			return this.target.getLevel();
		}
	}

	public static class DataSafe extends Data {
		public DataSafe( LivingEntity target, boolean loadedFromDisk ) {
			super( target, loadedFromDisk );
		}
	}
}