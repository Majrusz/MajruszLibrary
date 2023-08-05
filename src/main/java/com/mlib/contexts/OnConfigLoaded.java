package com.mlib.contexts;

import com.mlib.config.ConfigHandler;
import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;

import java.util.function.Consumer;

public class OnConfigLoaded {
	public static Context< Data > listen( Consumer< Data > consumer ) {
		return Contexts.get( Data.class ).add( consumer );
	}

	public static Data dispatch( ConfigHandler configHandler ) {
		return Contexts.get( Data.class ).dispatch( new Data( configHandler ) );
	}

	public static class Data {
		public final ConfigHandler configHandler;

		public Data( ConfigHandler configHandler ) {
			this.configHandler = configHandler;
		}
	}
}