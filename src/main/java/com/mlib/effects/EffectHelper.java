package com.mlib.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;

/** Methods for easier handling effects. */
public class EffectHelper {
	/**
	 Adds potion effect to entity only when effect is applicable.

	 @param entity         Entity to add potion effect.
	 @param effectInstance Instance of desired effect.
	 */
	public static void applyEffectIfPossible( LivingEntity entity, EffectInstance effectInstance ) {
		if( entity.isPotionApplicable( effectInstance ) )
			entity.addPotionEffect( effectInstance );
	}

	/**
	 Adds potion effect to entity only when effect is applicable.

	 @param entity         Entity to add potion effect.
	 @param effect         Desired effect.
	 @param effectDuration Effect duration in ticks.
	 @param amplifier      Effect amplifier/level.
	 */
	public static void applyEffectIfPossible( LivingEntity entity, Effect effect, int effectDuration, int amplifier ) {
		applyEffectIfPossible( entity, new EffectInstance( effect, effectDuration, amplifier ) );
	}
}
