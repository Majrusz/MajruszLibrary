package com.majruszlibrary.level;

import com.majruszlibrary.math.AnyPos;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.NetherWartBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class BlockHelper {
	public static boolean isCropAtMaxAge( BlockState blockState ) {
		Block block = blockState.getBlock();
		if( block instanceof CropBlock cropBlock ) {
			return cropBlock.isMaxAge( blockState );
		} else if( block instanceof NetherWartBlock wartBlock ) {
			return blockState.getValue( NetherWartBlock.AGE ) >= 3;
		}

		return false;
	}

	public static boolean isCropAtMaxAge( Level level, BlockPos position ) {
		return BlockHelper.isCropAtMaxAge( level.getBlockState( position ) );
	}

	public static void growCrop( Level level, BlockPos position ) {
		BlockState blockState = level.getBlockState( position );
		Block block = blockState.getBlock();
		if( block instanceof CropBlock cropBlock ) {
			cropBlock.growCrops( level, position, blockState );
		} else if( block instanceof NetherWartBlock wartBlock ) {
			int age = Math.min( blockState.getValue( NetherWartBlock.AGE ) + 1, 3 );
			level.setBlockAndUpdate( position, blockState.setValue( NetherWartBlock.AGE, age ) );
		}
	}

	public static BlockState getState( Level level, BlockPos position ) {
		return level.getBlockState( position );
	}

	public static BlockState getState( Level level, Vec3 position ) {
		return BlockHelper.getState( level, AnyPos.from( position ).block() );
	}

	public static BlockEntity getEntity( Level level, BlockPos position ) {
		return level.getBlockEntity( position );
	}

	public static BlockEntity getEntity( Level level, Vec3 position ) {
		return BlockHelper.getEntity( level, AnyPos.from( position ).block() );
	}
}
