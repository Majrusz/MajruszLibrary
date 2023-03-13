package com.mlib.gamemodifiers.contexts;

import com.mlib.config.ConfigHandler;
import com.mlib.gamemodifiers.ContextBase;
import com.mlib.gamemodifiers.Contexts;

import java.util.function.Consumer;

public class OnConfigLoaded {
	public static ContextBase< Data > listen( Consumer< Data > consumer ) {
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