package com.majruszlibrary.contexts;

import com.majruszlibrary.contexts.base.Context;
import com.majruszlibrary.contexts.base.Contexts;

import java.util.function.Consumer;

public class OnGameInitialized {
	public static Context< OnGameInitialized > listen( Consumer< OnGameInitialized > consumer ) {
		return Contexts.get( OnGameInitialized.class ).add( consumer );
	}

	public OnGameInitialized() {}
}
