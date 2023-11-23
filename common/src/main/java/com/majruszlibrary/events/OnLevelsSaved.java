package com.majruszlibrary.events;

import com.majruszlibrary.events.base.Event;
import com.majruszlibrary.events.base.Events;
import net.minecraft.server.MinecraftServer;

import java.util.function.Consumer;

public class OnLevelsSaved {
	public final MinecraftServer server;

	public static Event< OnLevelsSaved > listen( Consumer< OnLevelsSaved > consumer ) {
		return Events.get( OnLevelsSaved.class ).add( consumer );
	}

	public OnLevelsSaved( MinecraftServer server ) {
		this.server = server;
	}
}
