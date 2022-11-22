package com.mlib.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

/** Methods for easier handling effects. */
@Deprecated( since = "Since 2.17.0 use MobEffectHelper" )
public class EffectHelper {
	@Deprecated( since = "Since 2.17.0 use MobEffectHelper" )
	public static boolean applyEffectIfPossible( LivingEntity entity, MobEffectInstance effectInstance ) {
		if( entity.canBeAffected( effectInstance ) ) {
			entity.addEffect( effectInstance );
			return true;
		}

		return false;
	}

	@Deprecated( since = "Since 2.17.0 use MobEffectHelper" )
	public static boolean applyEffectIfPossible( LivingEntity entity, MobEffect effect, int ticks, int amplifier ) {
		return applyEffectIfPossible( entity, new MobEffectInstance( effect, ticks, amplifier ) );
	}

	@Deprecated( since = "Since 2.17.0 use MobEffectHelper" )
	public static boolean stackEffectIfPossible( LivingEntity entity, MobEffect effect, int ticks, int amplifier, int maxTicks ) {
		MobEffectInstance previousEffectInstance = entity.getEffect( effect );
		if( previousEffectInstance == null || previousEffectInstance.getAmplifier() > amplifier )
			return applyEffectIfPossible( entity, effect, ticks, amplifier );

		ticks = Math.min( ticks + previousEffectInstance.getDuration(), maxTicks );
		return applyEffectIfPossible( entity, effect, ticks, amplifier );
	}

	@Deprecated( since = "Since 2.17.0 use MobEffectHelper" )
	public static boolean stackEffectIfPossible( LivingEntity entity, MobEffectInstance effectInstance, int maxTicks ) {
		return stackEffectIfPossible( entity, effectInstance.getEffect(), effectInstance.getDuration(), effectInstance.getAmplifier(), maxTicks );
	}

	@Deprecated( since = "Since 2.17.0 use MobEffectHelper" )
	/** Returns effect amplifier or -1 if entity does not have this effect active. */
	public static int getEffectAmplifier( LivingEntity entity, MobEffect effect ) {
		MobEffectInstance effectInstance = entity.getEffect( effect );
		if( effectInstance != null )
			return effectInstance.getAmplifier();

		return -1;
	}
}
