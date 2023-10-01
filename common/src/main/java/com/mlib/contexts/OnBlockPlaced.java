package com.mlib.contexts;

import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;
import com.mlib.contexts.data.ILevelData;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Consumer;

public class OnBlockPlaced {
	public static Context< Data > listen( Consumer< Data > consumer ) {
		return Contexts.get( Data.class ).add( consumer );
	}

	public static Data dispatch( ServerLevel level, BlockPos position, BlockState blockState ) {
		return Contexts.get( Data.class ).dispatch( new Data( level, position, blockState ) );
	}

	public static class Data implements ILevelData {
		private final ServerLevel level;
		private final BlockPos position;
		private final BlockState blockState;

		public Data( ServerLevel level, BlockPos position, BlockState blockState ) {
			this.level = level;
			this.position = position;
			this.blockState = blockState;
		}

		@Override
		public Level getLevel() {
			return this.level;
		}
	}
}
