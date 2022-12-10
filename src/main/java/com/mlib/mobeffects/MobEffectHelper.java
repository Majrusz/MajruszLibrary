package com.mlib.mobeffects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public class MobEffectHelper {
	public static boolean tryToApply( LivingEntity entity, MobEffectInstance effectInstance ) {
		if( entity.canBeAffected( effectInstance ) ) {
			entity.addEffect( effectInstance );
			return true;
		}

		return false;
	}

	public static boolean tryToApply( LivingEntity entity, MobEffect effect, int ticks, int amplifier ) {
		return tryToApply( entity, new MobEffectInstance( effect, ticks, amplifier ) );
	}

	public static boolean tryToStack( LivingEntity entity, MobEffect effect, int ticks, int amplifier, int maxTicks ) {
		MobEffectInstance previousEffectInstance = entity.getEffect( effect );
		if( previousEffectInstance == null || previousEffectInstance.getAmplifier() > amplifier )
			return tryToApply( entity, effect, ticks, amplifier );

		ticks = Math.min( ticks + previousEffectInstance.getDuration(), maxTicks );
		return tryToApply( entity, effect, ticks, amplifier );
	}

	public static boolean tryToStack( LivingEntity entity, MobEffectInstance effectInstance, int maxTicks ) {
		return tryToStack( entity, effectInstance.getEffect(), effectInstance.getDuration(), effectInstance.getAmplifier(), maxTicks );
	}

	public static boolean tryToStackAmplifier( LivingEntity entity, MobEffect effect, int ticks, int amplifier, int maxAmplifier ) {
		MobEffectInstance previousEffectInstance = entity.getEffect( effect );
		if( previousEffectInstance == null )
			return tryToApply( entity, effect, ticks, amplifier );

		amplifier = Math.min( amplifier + previousEffectInstance.getAmplifier() + 1, maxAmplifier );
		return tryToApply( entity, effect, ticks, amplifier );
	}

	public static boolean tryToStackAmplifier( LivingEntity entity, MobEffectInstance effectInstance, int maxAmplifier ) {
		return tryToStackAmplifier( entity, effectInstance.getEffect(), effectInstance.getDuration(), effectInstance.getAmplifier(), maxAmplifier );
	}

	public static int getAmplifier( LivingEntity entity, MobEffect effect ) {
		MobEffectInstance effectInstance = entity.getEffect( effect );

		return effectInstance != null ? effectInstance.getAmplifier() : -1 ;
	}
}
