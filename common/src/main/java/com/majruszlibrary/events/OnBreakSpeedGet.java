package com.majruszlibrary.events;

import com.majruszlibrary.events.base.Event;
import com.majruszlibrary.events.base.Events;
import com.majruszlibrary.events.type.IEntityEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Consumer;

public class OnBreakSpeedGet implements IEntityEvent {
	public final Player player;
	public final BlockState blockState;
	public final float original;
	public float speed;

	public static Event< OnBreakSpeedGet > listen( Consumer< OnBreakSpeedGet > consumer ) {
		return Events.get( OnBreakSpeedGet.class ).add( consumer );
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