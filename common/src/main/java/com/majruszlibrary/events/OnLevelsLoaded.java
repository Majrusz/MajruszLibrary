package com.majruszlibrary.events;

import com.majruszlibrary.events.base.Event;
import com.majruszlibrary.events.base.Events;
import net.minecraft.server.MinecraftServer;

import java.util.function.Consumer;

public class OnLevelsLoaded {
	public final MinecraftServer server;

	public static Event< OnLevelsLoaded > listen( Consumer< OnLevelsLoaded > consumer ) {
		return Events.get( OnLevelsLoaded.class ).add( consumer );
	}

	public OnLevelsLoaded( MinecraftServer server ) {
		this.server = server;
	}
}
