package com.mlib.contexts;

import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;

import java.util.function.Consumer;

public class OnClientTick {
	public static Context< OnClientTick > listen( Consumer< OnClientTick > consumer ) {
		return Contexts.get( OnClientTick.class ).add( consumer );
	}

	public OnClientTick() {}
}
