package com.mlib.damage;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;

/** Methods for easier damage and damage source handling. */
public class DamageHelper {
	/** Returns given entity class from damage source or null if casting was impossible. */
	@Nullable
	public static < EntityClass extends Entity > EntityClass getEntityFromDamageSource( Class< EntityClass > entityClass, DamageSource source ) {
		return entityClass.isInstance( source.getEntity() ) ? entityClass.cast( source.getEntity() ) : null;
	}

	/**
	 Returns PlayerEntity from given damage source. (true source)
	 Returns null if casting was impossible.
	 */
	@Nullable
	public static Player getPlayerFromDamageSource( DamageSource damageSource ) {
		return damageSource.getEntity() instanceof Player ? ( Player )damageSource.getEntity() : null;
	}

	/**
	 Checks whether damage source from directly from living entity.
	 For example if skeleton shoots an arrow then it is not direct damage.
	 */
	public static boolean isDirectDamageFromLivingEntity( DamageSource source ) {
		return source.getEntity() instanceof LivingEntity && source.getDirectEntity() instanceof LivingEntity;
	}
}
