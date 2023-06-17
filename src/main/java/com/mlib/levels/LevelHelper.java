package com.mlib.levels;

import com.mlib.Random;
import com.mlib.effects.ParticleHandler;
import com.mlib.effects.SoundHandler;
import com.mlib.math.AnyPos;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FrostedIceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.storage.ServerLevelData;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;

import java.util.Optional;

public class LevelHelper {
	public static boolean isEntityIn( Entity entity, ResourceKey< Level > worldRegistryKey ) {
		return entity.level().dimension() == worldRegistryKey;
	}

	public static DifficultyInstance getDifficultyAt( Entity entity ) {
		return getDifficultyAt( entity.level(), entity.blockPosition() );
	}

	public static DifficultyInstance getDifficultyAt( Level level, BlockPos position ) {
		return level.getCurrentDifficultyAt( position );
	}

	public static double getRegionalDifficultyAt( Entity entity ) {
		return getRegionalDifficultyAt( entity.level(), entity.blockPosition() );
	}

	public static double getRegionalDifficultyAt( Level level, BlockPos position ) {
		return getDifficultyAt( level, position ).getEffectiveDifficulty();
	}

	public static double getClampedRegionalDifficultyAt( Entity entity ) {
		return getClampedRegionalDifficultyAt( entity.level(), entity.blockPosition() );
	}

	public static double getClampedRegionalDifficultyAt( Level level, BlockPos position ) {
		return getDifficultyAt( level, position ).getSpecialMultiplier();
	}

	public static boolean isEntityOutside( Entity entity ) {
		return entity.level().canSeeSky( entity.blockPosition() );
	}

	public static boolean isRainingAt( Entity entity ) {
		return entity.level().isRainingAt( entity.blockPosition() );
	}

	public static boolean isDayAt( Entity entity ) {
		return entity.level().isDay();
	}

	public static boolean isNightAt( Entity entity ) {
		return entity.level().isNight();
	}

	public static Pair< Vec3, ServerLevel > getSpawnData( ServerPlayer player ) {
		BlockPos respawnPosition = player.getRespawnPosition();
		ServerLevel serverLevel = player.server.getLevel( player.getRespawnDimension() );
		Vec3 exactSpawnPosition = null;
		if( serverLevel == null )
			return new Pair<>( Vec3.ZERO, null );

		if( respawnPosition != null ) {
			float angle = player.getRespawnAngle();
			Optional< Vec3 > spawnPosition = Player.findRespawnPositionAndUseSpawnBlock( serverLevel, respawnPosition, angle, true, true );
			if( spawnPosition.isPresent() )
				exactSpawnPosition = spawnPosition.get();
		}
		if( exactSpawnPosition == null ) {
			serverLevel = player.server.getLevel( Level.OVERWORLD );
			assert serverLevel != null;
			exactSpawnPosition = AnyPos.from( serverLevel.getSharedSpawnPos() ).vec3();
		}

		return new Pair<>( exactSpawnPosition, serverLevel );
	}

	public static void teleportToSpawnPosition( ServerPlayer player ) {
		Pair< Vec3, ServerLevel > spawnData = getSpawnData( player );
		Vec3 spawnPosition = spawnData.getFirst();
		ServerLevel serverLevel = spawnData.getSecond();

		player.teleportTo( serverLevel, spawnPosition.x, spawnPosition.y, spawnPosition.z, player.getYRot(), player.getXRot() );
	}

	public static boolean teleportNearby( LivingEntity target, ServerLevel level, double offset ) {
		boolean isEntityInside = target.yOld + 8 > level.getHeight( Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ( int )target.xOld, ( int )target.zOld );
		if( isEntityInside )
			offset /= 2;

		Vec3 newPosition = Random.getRandomVector( -offset, offset, -1.0, 1.0, -offset, offset ).add( target.position() ).vec3();
		double y = level.getHeight( Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ( int )newPosition.x, ( int )newPosition.z ) + 1;
		if( !( y < level.getMinBuildHeight() + 1 ) && target.randomTeleport( newPosition.x, target.yOld + 8 > y ? y : newPosition.y, newPosition.z, true ) ) {
			Vec3 position = new Vec3( target.xo, target.getY( 0.5 ), target.zo );
			SoundHandler.ENDERMAN_TELEPORT.play( level, position );
			ParticleHandler.PORTAL.spawn( level, position, 10 );
			return true;
		} else {
			return false;
		}
	}

	public static void freezeWater( Entity entity, double radius, int minimumIceDuration, int maximumIceDuration ) {
		freezeWater( entity, radius, minimumIceDuration, maximumIceDuration, true );
	}

	public static void freezeWater( Entity entity, double radius, int minimumIceDuration, int maximumIceDuration, boolean requireOnGround ) {
		if( requireOnGround && !entity.onGround() )
			return;

		BlockState iceBlockState = Blocks.FROSTED_ICE.defaultBlockState();
		BlockPos entityPosition = entity.blockPosition();
		BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
		Iterable< BlockPos > blocksInRange = BlockPos.betweenClosed( entityPosition.offset( ( int )-radius, -1, ( int )-radius ), entityPosition.offset( ( int )radius, -1, ( int )radius ) );

		for( BlockPos blockPos : blocksInRange ) {
			if( blockPos.closerThan( entityPosition, radius ) ) {
				mutableBlockPos.set( blockPos.getX(), blockPos.getY() + 1, blockPos.getZ() );
				BlockState blockState = entity.level().getBlockState( mutableBlockPos );
				if( blockState.isAir() ) {
					BlockState blockState2 = entity.level().getBlockState( blockPos );
					boolean meltsIntoFrostedIceBlock = blockState2 == FrostedIceBlock.meltsInto();
					boolean canSurvive = iceBlockState.canSurvive( entity.level(), blockPos );
					boolean isUnobstructed = entity.level().isUnobstructed( iceBlockState, blockPos, CollisionContext.empty() );

					if( meltsIntoFrostedIceBlock && canSurvive && isUnobstructed && !net.minecraftforge.event.ForgeEventFactory.onBlockPlace( entity, net.minecraftforge.common.util.BlockSnapshot.create( entity.level()
						.dimension(), entity.level(), blockPos ), net.minecraft.core.Direction.UP ) ) {
						entity.level().setBlockAndUpdate( blockPos, iceBlockState );
						entity.level()
							.scheduleTick( blockPos, Blocks.FROSTED_ICE, Mth.nextInt( Random.getThreadSafe(), minimumIceDuration, maximumIceDuration ) );
					}
				}
			}
		}
	}

	public static void spawnItemEntityFlyingTowardsDirection( ItemStack itemStack, Level level, Vec3 from, Vec3 to ) {
		Vec3 spawnPosition = AnyPos.from( from ).add( Random.getRandomVector( -0.25, 0.25, 0.125, 0.5, -0.25, 0.25 ) ).vec3();
		Vec3 motion = AnyPos.from( to ).sub( spawnPosition ).mul( 0.1 ).vec3();

		ItemEntity itemEntity = new ItemEntity( level, spawnPosition.x, spawnPosition.y, spawnPosition.z, itemStack );
		itemEntity.setDeltaMovement( AnyPos.from( motion ).add( 0.0, Math.pow( AnyPos.from( motion ).len().doubleValue(), 0.5 ) * 0.25, 0.0 ).vec3() );

		level.addFreshEntity( itemEntity );
	}

	public static void startRaining( Level level, int ticks, boolean withThunder ) {
		if( !( level.getLevelData() instanceof ServerLevelData data ) )
			return;

		data.setRaining( true );
		data.setRainTime( ticks );
		if( withThunder ) {
			data.setThundering( true );
			data.setThunderTime( ticks );
		}
	}

	public static void startRaining( Level level, int ticks ) {
		startRaining( level, ticks, false );
	}

	public static void setClearWeather( Level level, int ticks ) {
		if( !( level.getLevelData() instanceof ServerLevelData data ) )
			return;

		data.setRaining( false );
		data.setRainTime( ticks );
		data.setThundering( false );
		data.setThunderTime( ticks );
	}
}
