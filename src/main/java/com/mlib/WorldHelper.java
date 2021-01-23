package com.mlib;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.server.ServerWorld;

/** Simple class for more explicit way to handling world functions. */
public class WorldHelper {
	/** Checks whether the entity is in a particular dimension. */
	public static boolean isEntityIn( Entity entity, RegistryKey< World > worldRegistryKey ) {
		return entity.world.getDimensionKey() == worldRegistryKey;
	}

	/**
	 Returns difficulty instance of position where entity is currently at.

	 @param entity Entity to get difficulty instance.
	 */
	public static DifficultyInstance getDifficultyInstance( Entity entity ) {
		return entity.world.getDifficultyForLocation( new BlockPos( entity.getPositionVec() ) );
	}

	/**
	 Returns current regional difficulty of the entity.

	 @param entity Entity where difficulty should be calculated.
	 */
	public static double getRegionalDifficulty( Entity entity ) {
		DifficultyInstance difficultyInstance = getDifficultyInstance( entity );

		return difficultyInstance.getAdditionalDifficulty();
	}

	/**
	 Returns current clamped regional difficulty of the entity. (range [0.0;1.0])

	 @param entity Entity where difficulty should be calculated.
	 */
	public static double getClampedRegionalDifficulty( Entity entity ) {
		DifficultyInstance difficultyInstance = getDifficultyInstance( entity );

		return difficultyInstance.getClampedAdditionalDifficulty();
	}

	/**
	 Checks whether entity is outside when it is raining.

	 @param entity Entity to check.
	 */
	public static boolean isEntityOutsideWhenItIsRaining( Entity entity ) {
		World world = entity.world;
		BlockPos entityPosition = new BlockPos( entity.getPositionVec() );
		Biome biome = world.getBiome( entityPosition );

		return world.canSeeSky( entityPosition ) && world.isRaining() && biome.getPrecipitation() == Biome.RainType.RAIN;
	}
}
