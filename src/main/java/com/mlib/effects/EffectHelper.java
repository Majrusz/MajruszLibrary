package com.mlib.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

/** Methods for easier handling effects. */
public class EffectHelper {
	/**
	 Adds potion effect to entity only when effect is applicable.

	 @param entity         Entity to add potion effect.
	 @param effectInstance Instance of desired effect.
	 */
	public static void applyEffectIfPossible( LivingEntity entity, MobEffectInstance effectInstance ) {
		if( entity.canBeAffected( effectInstance ) )
			entity.addEffect( effectInstance );
	}

	/**
	 Adds potion effect to entity only when effect is applicable.

	 @param entity         Entity to add potion effect.
	 @param effect         Desired effect.
	 @param effectDuration Effect duration in ticks.
	 @param amplifier      Effect amplifier/level.
	 */
	public static void applyEffectIfPossible( LivingEntity entity, MobEffect effect, int effectDuration, int amplifier ) {
		applyEffectIfPossible( entity, new MobEffectInstance( effect, effectDuration, amplifier ) );
	}
}
