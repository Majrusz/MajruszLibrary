package com.majruszlibrary.events;

import com.majruszlibrary.events.base.Event;
import com.majruszlibrary.events.base.Events;
import com.majruszlibrary.events.type.IEntityEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

import java.util.function.Consumer;

public class OnPlayerLoggedOut implements IEntityEvent {
	public final ServerPlayer player;

	public static Event< OnPlayerLoggedOut > listen( Consumer< OnPlayerLoggedOut > consumer ) {
		return Events.get( OnPlayerLoggedOut.class ).add( consumer );
	}

	public OnPlayerLoggedOut( ServerPlayer player ) {
		this.player = player;
	}

	@Override
	public Entity getEntity() {
		return this.player;
	}
}
