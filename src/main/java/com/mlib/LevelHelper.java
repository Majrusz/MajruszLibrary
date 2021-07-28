package com.mlib;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;

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

	/** Freeze water under given entity. */
	public static void freezeWater( LivingEntity entity, double radius, int minimumIceDuration, int maximumIceDuration ) {
		if( !entity.isOnGround() )
			return;

		BlockState iceBlockState = Blocks.FROSTED_ICE.defaultBlockState();
		BlockPos entityPosition = entity.getOnPos();
		BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
		Iterable< BlockPos > blocksInRange = BlockPos.betweenClosed( entityPosition.offset( -radius, -1.0, -radius ),
			entityPosition.offset( radius, -1.0, radius )
		);

		for( BlockPos blockPos : blocksInRange ) {
			if( blockPos.closerThan( entityPosition, radius ) ) {
				mutableBlockPos.set( blockPos.getX(), blockPos.getY() + 1, blockPos.getZ() );
				BlockState blockState = entity.level.getBlockState( mutableBlockPos );
				if( blockState.isAir() ) {
					BlockState blockState2 = entity.level.getBlockState( blockPos );
					boolean isWater = blockState2.getMaterial() == Material.WATER;
					boolean isFull = blockState2.getBlock() == Blocks.WATER && blockState2.getValue( LiquidBlock.LEVEL ) == 0;
					boolean canSurvive = iceBlockState.canSurvive( entity.level, blockPos );
					boolean isUnobstructed = entity.level.isUnobstructed( iceBlockState, blockPos, CollisionContext.empty() );

					if( isWater && isFull && canSurvive && isUnobstructed && !net.minecraftforge.event.ForgeEventFactory.onBlockPlace( entity,
						net.minecraftforge.common.util.BlockSnapshot.create( entity.level.dimension(), entity.level, blockPos ),
						net.minecraft.core.Direction.UP
					) ) {
						entity.level.setBlockAndUpdate( blockPos, iceBlockState );
						entity.level.getBlockTicks()
							.scheduleTick( blockPos, Blocks.FROSTED_ICE, Mth.nextInt( entity.getRandom(), minimumIceDuration, maximumIceDuration ) );
					}
				}
			}
		}
	}
}
