package com.majruszlibrary.events;

import com.majruszlibrary.events.base.Event;
import com.majruszlibrary.events.base.Events;

import java.util.function.Consumer;

public class OnResourcesReloaded {
	public static Event< OnResourcesReloaded > listen( Consumer< OnResourcesReloaded > consumer ) {
		return Events.get( OnResourcesReloaded.class ).add( consumer );
	}

	public OnResourcesReloaded() {}
}
