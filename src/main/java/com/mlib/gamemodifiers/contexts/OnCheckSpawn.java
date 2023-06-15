package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.Context;
import com.mlib.gamemodifiers.Contexts;
import com.mlib.gamemodifiers.data.IEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

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

		public Data( LivingSpawnEvent.CheckSpawn event ) {
			this.event = event;
			this.mob = event.getEntityLiving();
		}

		@Override
		public Entity getEntity() {
			return this.mob;
		}
	}
}