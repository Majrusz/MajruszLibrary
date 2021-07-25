package com.mlib;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Optional;

/** Simple class for more explicit way to handling world functions. */
public class LevelHelper {
	/** Checks whether the entity is in a particular dimension. */
	public static boolean isEntityIn( Entity entity, ResourceKey< Level > worldRegistryKey ) {
		return entity.level.dimension() == worldRegistryKey;
	}

	/**
	 Returns difficulty instance of position where entity is currently at.

	 @param entity Entity to get difficulty instance.
	 */
	public static DifficultyInstance getDifficultyInstance( Entity entity ) {
		return entity.level.getCurrentDifficultyAt( new BlockPos( entity.position() ) );
	}

	/**
	 Returns current regional difficulty of the entity.

	 @param entity Entity where difficulty should be calculated.
	 */
	public static double getRegionalDifficulty( Entity entity ) {
		DifficultyInstance difficultyInstance = getDifficultyInstance( entity );

		return difficultyInstance.getEffectiveDifficulty();
	}

	/**
	 Returns current clamped regional difficulty of the entity. (range [0.0;1.0])

	 @param entity Entity where difficulty should be calculated.
	 */
	public static double getClampedRegionalDifficulty( Entity entity ) {
		DifficultyInstance difficultyInstance = getDifficultyInstance( entity );

		return difficultyInstance.getSpecialMultiplier();
	}

	/**
	 Checks whether entity is outside.

	 @param entity Entity to check.
	 */
	public static boolean isEntityOutside( Entity entity ) {
		Level level = entity.level;

		return level.canSeeSky( new BlockPos( entity.position() ) );
	}

	/**
	 Checks whether is raining at entity current biome.

	 @param entity Entity to check.
	 */
	public static boolean isRainingAtEntityBiome( Entity entity ) {
		Level level = entity.level;
		Biome biome = level.getBiome( new BlockPos( entity.position() ) );

		return level.isRaining() && biome.getPrecipitation() == Biome.Precipitation.RAIN;
	}

	/**
	 Checks whether entity is outside when it is raining.

	 @param entity Entity to check.
	 */
	public static boolean isEntityOutsideWhenItIsRaining( Entity entity ) {
		return isEntityOutside( entity ) && isRainingAtEntityBiome( entity );
	}

	/**
	 Checks whether entity is outside during the day.

	 @param entity Entity to check.
	 */
	public static boolean isEntityOutsideDuringTheDay( Entity entity ) {
		return isEntityOutside( entity ) && entity.level.isDay();
	}

	/**
	 Checks whether entity is outside during the night.

	 @param entity Entity to check.
	 */
	public static boolean isEntityOutsideDuringTheNight( Entity entity ) {
		return isEntityOutside( entity ) && entity.level.isNight();
	}

	/**
	 Returns ServerLevel from given entity.
	 Returns null if casting was impossible.
	 */
	@Nullable
	public static ServerLevel getServerLevelFromEntity( Entity entity ) {
		return entity.level instanceof ServerLevel ? ( ServerLevel )entity.level : null;
	}

	/** Returns player spawn position. (bed position, nether anchor or world spawn point) */
	public static BlockPos getSpawnPosition( ServerPlayer player, ServerLevel level ) {
		BlockPos position = player.getRespawnPosition();
		Optional< BlockPos > spawnPosition = Optional.empty();
		if( position != null ) {
			Optional< Vec3 > temporaryPosition = ServerPlayer.findRespawnPositionAndUseSpawnBlock( level, position, player.getRespawnAngle(),
				player.isRespawnForced(), true
			);
			if( temporaryPosition.isPresent() )
				spawnPosition = Optional.of( new BlockPos( temporaryPosition.get() ) );
		}

		return spawnPosition.orElseGet( level::getSharedSpawnPos );
	}
}
