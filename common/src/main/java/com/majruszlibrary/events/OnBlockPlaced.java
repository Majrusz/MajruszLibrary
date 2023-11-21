package com.majruszlibrary.events;

import com.majruszlibrary.events.base.Event;
import com.majruszlibrary.events.base.Events;
import com.majruszlibrary.events.type.ILevelEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Consumer;

public class OnBlockPlaced implements ILevelEvent {
	public final ServerLevel level;
	public final BlockPos position;
	public final BlockState blockState;

	public static Event< OnBlockPlaced > listen( Consumer< OnBlockPlaced > consumer ) {
		return Events.get( OnBlockPlaced.class ).add( consumer );
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
