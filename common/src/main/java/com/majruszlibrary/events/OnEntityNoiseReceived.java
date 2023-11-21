package com.majruszlibrary.events;

import com.majruszlibrary.events.base.Event;
import com.majruszlibrary.events.base.Events;
import com.majruszlibrary.events.type.IEntityEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class OnEntityNoiseReceived implements IEntityEvent {
	public final ServerLevel level;
	public final BlockPos position;
	public final Entity listener;
	public final @Nullable Entity emitter;
	public final @Nullable Entity projectile;
	public final float distance;

	public static Event< OnEntityNoiseReceived > listen( Consumer< OnEntityNoiseReceived > consumer ) {
		return Events.get( OnEntityNoiseReceived.class ).add( consumer );
	}

	public OnEntityNoiseReceived( ServerLevel level, BlockPos position, Entity listener, @Nullable Entity emitter, @Nullable Entity projectile, float distance
	) {
		this.level = level;
		this.position = position;
		this.listener = listener;
		this.emitter = emitter;
		this.projectile = projectile;
		this.distance = distance;
	}

	@Override
	public Entity getEntity() {
		return this.listener;
	}
}
