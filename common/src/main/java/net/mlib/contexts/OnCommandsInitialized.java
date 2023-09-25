package net.mlib.contexts;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.mlib.contexts.base.Context;
import net.mlib.contexts.base.Contexts;

import java.util.function.Consumer;

public class OnCommandsInitialized {
	public static Context< Data > listen( Consumer< Data > consumer ) {
		return Contexts.get( Data.class ).add( consumer );
	}

	public static void dispatch( CommandDispatcher< CommandSourceStack > dispatcher ) {
		Contexts.get( Data.class ).dispatch( new Data( dispatcher ) );
	}

	public static class Data {
		public final CommandDispatcher< CommandSourceStack > dispatcher;

		public Data( CommandDispatcher< CommandSourceStack > dispatcher ) {
			this.dispatcher = dispatcher;
		}
	}
}
