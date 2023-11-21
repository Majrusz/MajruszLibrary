package com.majruszlibrary.events;

import com.majruszlibrary.events.base.Event;
import com.majruszlibrary.events.base.Events;

import java.util.function.Consumer;

public class OnServerTicked {
	public static Event< OnServerTicked > listen( Consumer< OnServerTicked > consumer ) {
		return Events.get( OnServerTicked.class ).add( consumer );
	}

	public OnServerTicked() {}
}
