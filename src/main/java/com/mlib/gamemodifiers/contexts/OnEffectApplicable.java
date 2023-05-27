package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.Context;
import com.mlib.gamemodifiers.Contexts;
import com.mlib.gamemodifiers.data.IEntityData;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class OnEffectApplicable {
	public static Context< Data > listen( Consumer< Data > consumer ) {
		return Contexts.get( Data.class ).add( consumer );
	}

	@SubscribeEvent
	public static void onEffectApplicable( MobEffectEvent.Applicable event ) {
		Contexts.get( Data.class ).dispatch( new Data( event ) );
	}

	public static class Data implements IEntityData {
		public final MobEffectEvent.Applicable event;
		public final MobEffectInstance effectInstance;
		public final MobEffect effect;
		public final LivingEntity entity;

		public Data( MobEffectEvent.Applicable event ) {
			this.event = event;
			this.effectInstance = event.getEffectInstance();
			this.effect = this.effectInstance.getEffect();
			this.entity = event.getEntity();
		}

		@Override
		public Entity getEntity() {
			return this.entity;
		}
	}
}
