package com.mlib.contexts;

import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;

import java.util.function.Consumer;

public class OnCommandsInitialized {
	public static Context< Data > listen( Consumer< Data > consumer ) {
		return Contexts.get( Data.class ).add( consumer );
	}

	public static Data dispatch( CommandDispatcher< CommandSourceStack > dispatcher ) {
		return Contexts.get( Data.class ).dispatch( new Data( dispatcher ) );
	}

	public static class Data {
		public final CommandDispatcher< CommandSourceStack > dispatcher;

		public Data( CommandDispatcher< CommandSourceStack > dispatcher ) {
			this.dispatcher = dispatcher;
		}
	}
}
