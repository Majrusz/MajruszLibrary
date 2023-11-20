package com.majruszlibrary.contexts;

import com.majruszlibrary.contexts.base.Context;
import com.majruszlibrary.contexts.base.Contexts;

import java.util.function.Consumer;

public class OnClientTicked {
	public static Context< OnClientTicked > listen( Consumer< OnClientTicked > consumer ) {
		return Contexts.get( OnClientTicked.class ).add( consumer );
	}

	public OnClientTicked() {}
}
