package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.Context;
import com.mlib.gamemodifiers.Contexts;
import com.mlib.gamemodifiers.data.IEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class OnChorusFruitTeleport {
	public static Context< Data > listen( Consumer< Data > consumer ) {
		return Contexts.get( Data.class ).add( consumer );
	}

	@SubscribeEvent
	public static void onTeleport( EntityTeleportEvent.ChorusFruit event ) {
		Contexts.get( Data.class ).dispatch( new Data( event ) );
	}

	public static class Data implements IEntityData {
		public final EntityTeleportEvent.ChorusFruit event;
		public final LivingEntity entity;

		public Data( EntityTeleportEvent.ChorusFruit event ) {
			this.event = event;
			this.entity = event.getEntityLiving();
		}

		@Override
		public Entity getEntity() {
			return this.entity;
		}
	}
}
