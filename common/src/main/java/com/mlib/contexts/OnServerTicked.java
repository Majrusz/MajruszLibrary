package com.mlib.contexts;

import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;

import java.util.function.Consumer;

public class OnServerTicked {
	public static Context< OnServerTicked > listen( Consumer< OnServerTicked > consumer ) {
		return Contexts.get( OnServerTicked.class ).add( consumer );
	}

	public OnServerTicked() {}
}
