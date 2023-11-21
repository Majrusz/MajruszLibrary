package com.majruszlibrary.events;

import com.majruszlibrary.events.base.Event;
import com.majruszlibrary.events.base.Events;

import java.util.function.Consumer;

public class OnKeyStateChanged {
	public final int key;
	public final int scanCode;
	public final int action;
	public final int modifiers;

	public static Event< OnKeyStateChanged > listen( Consumer< OnKeyStateChanged > consumer ) {
		return Events.get( OnKeyStateChanged.class ).add( consumer );
	}

	public OnKeyStateChanged( int key, int scanCode, int action, int modifiers ) {
		this.key = key;
		this.scanCode = scanCode;
		this.action = action;
		this.modifiers = modifiers;
	}
}
