package com.mlib.contexts;

import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;

import java.util.function.Consumer;

public class OnGameInitialized {
	public static Context< OnGameInitialized > listen( Consumer< OnGameInitialized > consumer ) {
		return Contexts.get( OnGameInitialized.class ).add( consumer );
	}

	public OnGameInitialized() {}
}
