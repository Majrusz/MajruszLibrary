package com.mlib.contexts;

import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;

import java.util.function.Consumer;

public class OnServerTick {
	public static Context< OnServerTick > listen( Consumer< OnServerTick > consumer ) {
		return Contexts.get( OnServerTick.class ).add( consumer );
	}

	public OnServerTick() {}
}
