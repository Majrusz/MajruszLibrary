package com.mlib.contexts;

import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;

import java.util.function.Consumer;

public class OnKeyPressed {
	public final int key;
	public final int scanCode;
	public final int action;
	public final int modifiers;

	public static Context< OnKeyPressed > listen( Consumer< OnKeyPressed > consumer ) {
		return Contexts.get( OnKeyPressed.class ).add( consumer );
	}

	public OnKeyPressed( int key, int scanCode, int action, int modifiers ) {
		this.key = key;
		this.scanCode = scanCode;
		this.action = action;
		this.modifiers = modifiers;
	}
}
