package com.mlib.contexts;

import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;
import com.mlib.contexts.data.IEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class OnCheckSpawn {
	public static final Consumer< Data > CANCEL = data->data.event.setSpawnCancelled( true );

	public static Context< Data > listen( Consumer< Data > consumer ) {
		return Contexts.get( Data.class ).add( consumer );
	}

	@SubscribeEvent
	public static void onSpawnCheck( MobSpawnEvent.FinalizeSpawn event ) {
		Contexts.get( Data.class ).dispatch( new Data( event ) );
	}

	public static class Data implements IEntityData {
		public final MobSpawnEvent.FinalizeSpawn event;
		public final Mob mob;
		public final MobSpawnType spawnType;

		public Data( MobSpawnEvent.FinalizeSpawn event ) {
			this.event = event;
			this.mob = event.getEntity();
			this.spawnType = event.getSpawnType();
		}

		@Override
		public Entity getEntity() {
			return this.mob;
		}

		@Nullable
		public final MobSpawnType getSpawnType() {
			return this.mob.getSpawnType();
		}
	}
}