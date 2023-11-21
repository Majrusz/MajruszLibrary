package com.majruszlibrary.events;

import com.majruszlibrary.events.base.Event;
import com.majruszlibrary.events.base.Events;
import com.majruszlibrary.events.type.IEntityEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;

import java.util.function.Consumer;

public class OnEntityNoiseCheck implements IEntityEvent {
	public final ServerLevel level;
	public final BlockPos position;
	public final Entity listener;
	private boolean isAudible = false;

	public static Event< OnEntityNoiseCheck > listen( Consumer< OnEntityNoiseCheck > consumer ) {
		return Events.get( OnEntityNoiseCheck.class ).add( consumer );
	}

	public OnEntityNoiseCheck( ServerLevel level, BlockPos position, Entity listener ) {
		this.level = level;
		this.position = position;
		this.listener = listener;
	}

	@Override
	public Entity getEntity() {
		return this.listener;
	}

	public void makeAudible() {
		this.isAudible = true;
	}

	public boolean isAudible() {
		return this.isAudible;
	}
}
