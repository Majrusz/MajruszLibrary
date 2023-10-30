package com.mlib.contexts;

import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;
import com.mlib.contexts.data.ILevelData;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Consumer;

public class OnBlockPlaced implements ILevelData {
	public final ServerLevel level;
	public final BlockPos position;
	public final BlockState blockState;

	public static Context< OnBlockPlaced > listen( Consumer< OnBlockPlaced > consumer ) {
		return Contexts.get( OnBlockPlaced.class ).add( consumer );
	}

	public OnBlockPlaced( ServerLevel level, BlockPos position, BlockState blockState ) {
		this.level = level;
		this.position = position;
		this.blockState = blockState;
	}

	@Override
	public Level getLevel() {
		return this.level;
	}
}
