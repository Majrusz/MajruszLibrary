package com.majruszlibrary.contexts;

import com.majruszlibrary.contexts.base.Context;
import com.majruszlibrary.contexts.base.Contexts;

import java.util.function.Consumer;

public class OnServerTicked {
	public static Context< OnServerTicked > listen( Consumer< OnServerTicked > consumer ) {
		return Contexts.get( OnServerTicked.class ).add( consumer );
	}

	public OnServerTicked() {}
}
