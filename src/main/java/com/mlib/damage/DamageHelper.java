package com.mlib.damage;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;

import javax.annotation.Nullable;

/** Methods for easier damage and damage source handling. */
public class DamageHelper {
	/**
	 Returns PlayerEntity from given damage source. (true source)
	 Returns null if casting was impossible.
	 */
	@Nullable
	public static PlayerEntity getPlayerFromDamageSource( DamageSource damageSource ) {
		return damageSource.getTrueSource() instanceof PlayerEntity ? ( PlayerEntity )damageSource.getTrueSource() : null;
	}

	/**
	 Checks whether damage source from directly from living entity.
	 For example if skeleton shoots an arrow then it is not direct damage.
	 */
	public static boolean isDirectDamageFromLivingEntity( DamageSource source ) {
		return source.getTrueSource() instanceof LivingEntity && source.getImmediateSource() instanceof LivingEntity;
	}
}
