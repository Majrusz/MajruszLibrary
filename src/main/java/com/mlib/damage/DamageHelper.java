package com.mlib.damage;

import com.mlib.CommonHelper;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;

/** Methods for easier damage and damage source handling. */
public class DamageHelper {
	/** Tries to cast the damage source entity to given class type or returns null otherwise. */
	@Nullable
	public static < NewType > NewType castEntityIfPossible( Class< NewType > newClass, DamageSource damageSource ) {
		return CommonHelper.castIfPossible( newClass, damageSource.getEntity() );
	}

	/** Tries to cast the damage source direct entity to given class type or returns null otherwise. */
	@Nullable
	public static < NewType > NewType castDirectEntityIfPossible( Class< NewType > newClass, DamageSource damageSource ) {
		return CommonHelper.castIfPossible( newClass, damageSource.getDirectEntity() );
	}

	/** Checks whether entity and direct entity from given damage sources are instances of given classes. */
	public static < Type1, Type2 > boolean areEntitiesInstancesOf( DamageSource damageSource, Class< Type1 > entityClass,
		Class< Type2 > directEntityClass
	) {
		return entityClass.isInstance( damageSource.getEntity() ) && directEntityClass.isInstance( damageSource.getDirectEntity() );
	}

	/** Returns given entity class from damage source or null if casting was impossible. */
	@Nullable
	@Deprecated
	public static < EntityClass extends Entity > EntityClass getEntityFromDamageSource( Class< EntityClass > entityClass, DamageSource source ) {
		return CommonHelper.castIfPossible( entityClass, source.getEntity() );
	}

	/**
	 Returns PlayerEntity from given damage source. (true source)
	 Returns null if casting was impossible.
	 */
	@Nullable
	@Deprecated
	public static Player getPlayerFromDamageSource( DamageSource damageSource ) {
		return CommonHelper.castIfPossible( Player.class, damageSource.getEntity() );
	}

	/**
	 Checks whether damage source from directly from living entity.
	 For example if skeleton shoots an arrow then it is not direct damage.
	 */
	@Deprecated
	public static boolean isDirectDamageFromLivingEntity( DamageSource source ) {
		return areEntitiesInstancesOf( source, LivingEntity.class, LivingEntity.class );
	}
}
