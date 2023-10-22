package com.mlib.contexts;

import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;
import com.mlib.contexts.data.IEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Consumer;

public class OnBreakSpeedGet implements IEntityData {
	public final Player player;
	public final BlockState blockState;
	public final float original;
	public float speed;

	public static Context< OnBreakSpeedGet > listen( Consumer< OnBreakSpeedGet > consumer ) {
		return Contexts.get( OnBreakSpeedGet.class ).add( consumer );
	}

	public OnBreakSpeedGet( Player player, BlockState blockState, float speed ) {
		this.player = player;
		this.blockState = blockState;
		this.original = speed;
		this.speed = speed;
	}

	@Override
	public Entity getEntity() {
		return this.player;
	}
}