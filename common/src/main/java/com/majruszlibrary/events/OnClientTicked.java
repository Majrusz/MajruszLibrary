package com.majruszlibrary.events;

import com.majruszlibrary.events.base.Event;
import com.majruszlibrary.events.base.Events;

import java.util.function.Consumer;

public class OnClientTicked {
	public static Event< OnClientTicked > listen( Consumer< OnClientTicked > consumer ) {
		return Events.get( OnClientTicked.class ).add( consumer );
	}

	public OnClientTicked() {}
}
