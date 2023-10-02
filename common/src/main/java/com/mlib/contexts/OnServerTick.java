package com.mlib.contexts;

import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;

import java.util.function.Consumer;

public class OnServerTick {
	public static Context< Data > listen( Consumer< Data > consumer ) {
		return Contexts.get( Data.class ).add( consumer );
	}

	public static Data dispatch() {
		return Contexts.get( Data.class ).dispatch( new Data() );
	}

	public static class Data {
		public Data() {}
	}
}
