package com.mlib.entities;

import com.mlib.CommonHelper;
import com.mlib.math.AABBHelper;
import com.mlib.math.VectorHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Pillager;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;

/** Class with common code for entities. */
public class EntityHelper {
	/** Simulates cheating death like Totem of Undying does. */
	public static void cheatDeath( LivingEntity entity, float healthRatio, boolean shouldPlayEffects ) {
		entity.setHealth( entity.getMaxHealth() * healthRatio );

		if( shouldPlayEffects && entity.level instanceof ServerLevel ) {
			ServerLevel level = ( ServerLevel )entity.level;

			level.sendParticles( ParticleTypes.TOTEM_OF_UNDYING, entity.getX(), entity.getY( 0.75 ), entity.getZ(), 64, 0.25, 0.5, 0.25, 0.5 );
			level.playSound( null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.TOTEM_USE, SoundSource.AMBIENT, 1.0f, 1.0f );
		}
	}

	/**
	 Returns Player from given entity.
	 Returns null if casting was impossible.
	 */
	@Nullable
	public static Player getPlayerFromEntity( @Nullable Entity entity ) {
		return CommonHelper.castIfPossible( Player.class, entity );
	}

	/** Checks whether given player has Creative Mode enabled. */
	public static boolean isOnCreativeMode( Player player ) {
		return player.getAbilities().instabuild;
	}

	/** Returns whether given entity is humanoid. */
	public static boolean isHuman( Entity entity ) {
		return ( entity instanceof Villager || entity instanceof WanderingTrader || entity instanceof Player || entity instanceof Witch || entity instanceof Pillager );
	}

	/** Returns given entity's health ratio. */
	public static double getHealthRatio( LivingEntity entity ) {
		return Mth.clamp( entity.getHealth() / entity.getMaxHealth(), 0.0, 1.0 );
	}

	/** Returns given entity's missing health ratio. */
	public static double getMissingHealthRatio( LivingEntity entity ) {
		return 1.0 - getHealthRatio( entity );
	}

	/** Returns entities of a given class in a box with given side length. */
	public static < EntityType extends Entity > List< EntityType > getEntitiesInBox( Class< EntityType > entityClass, ServerLevel level, Vec3 position, double sideLength, Predicate< EntityType > extraPredicate ) {
		AABB axisAligned = AABBHelper.createInflatedAABB( position, sideLength / 2.0 );

		return level.getEntitiesOfClass( entityClass, axisAligned, extraPredicate );
	}

	/** Returns entities of a given class in a box with given side length. */
	public static < EntityType extends Entity > List< EntityType > getEntitiesInBox( Class< EntityType > entityClass, ServerLevel level, BlockPos blockPosition, double sideLength, Predicate< EntityType > extraPredicate ) {
		return getEntitiesInBox( entityClass, level, VectorHelper.convertToVec3( blockPosition ), sideLength, extraPredicate );
	}

	/** Returns entities of a given class in a box with given side length. */
	public static < EntityType extends Entity > List< EntityType > getEntitiesInBox( Class< EntityType > entityClass, ServerLevel level, Entity entity, double sideLength, Predicate< EntityType > extraPredicate ) {
		return getEntitiesInBox( entityClass, level, entity.position(), sideLength, extraPredicate );
	}

	/** Returns entities of a given class in a sphere with given radius. */
	public static < EntityType extends Entity > List< EntityType > getEntitiesInSphere( Class< EntityType > entityClass, ServerLevel level, Vec3 position, double radius, Predicate< EntityType > extraPredicate ) {
		Predicate< EntityType > distancePredicate = entity -> VectorHelper.distance( position, entity.position() ) <= radius;

		return getEntitiesInBox( entityClass, level, position, radius * 2.0, distancePredicate.and( extraPredicate ) );
	}

	/** Returns entities of a given class in a sphere with given radius. */
	public static < EntityType extends Entity > List< EntityType > getEntitiesInSphere( Class< EntityType > entityClass, ServerLevel level, BlockPos blockPosition, double radius, Predicate< EntityType > extraPredicate ) {
		return getEntitiesInSphere( entityClass, level, VectorHelper.convertToVec3( blockPosition ), radius, extraPredicate );
	}

	/** Returns entities of a given class in a sphere with given radius. */
	public static < EntityType extends Entity > List< EntityType > getEntitiesInSphere( Class< EntityType > entityClass, ServerLevel level, Entity entity, double radius, Predicate< EntityType > extraPredicate ) {
		return getEntitiesInSphere( entityClass, level, entity.position(), radius, extraPredicate );
	}
}
