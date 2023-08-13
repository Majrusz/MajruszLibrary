package com.mlib.contexts;

import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;
import com.mlib.contexts.data.IEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class OnCheckSpawn {
	public static final Consumer< Data > CANCEL = data->data.event.setResult( Event.Result.DENY );

	public static Context< Data > listen( Consumer< Data > consumer ) {
		return Contexts.get( Data.class ).add( consumer );
	}

	@SubscribeEvent
	public static void onSpawnCheck( LivingSpawnEvent.CheckSpawn event ) {
		Contexts.get( Data.class ).dispatch( new Data( event ) );
	}

	public static class Data implements IEntityData {
		public final LivingSpawnEvent.CheckSpawn event;
		public final LivingEntity mob;
		public final MobSpawnType spawnType;

		public Data( LivingSpawnEvent.CheckSpawn event ) {
			this.event = event;
			this.mob = event.getEntityLiving();
			this.spawnType = event.getSpawnReason();
		}

		@Override
		public Entity getEntity() {
			return this.mob;
		}

		@Nullable
		public final MobSpawnType getSpawnType() {
			return this.spawnType;
		}
	}
}