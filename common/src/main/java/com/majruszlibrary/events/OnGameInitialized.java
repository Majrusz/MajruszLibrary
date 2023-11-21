package com.majruszlibrary.events;

import com.majruszlibrary.events.base.Event;
import com.majruszlibrary.events.base.Events;

import java.util.function.Consumer;

public class OnGameInitialized {
	public static Event< OnGameInitialized > listen( Consumer< OnGameInitialized > consumer ) {
		return Events.get( OnGameInitialized.class ).add( consumer );
	}

	public OnGameInitialized() {}
}
