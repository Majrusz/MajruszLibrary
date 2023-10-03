package com.mlib.contexts;

import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;

import java.util.function.Consumer;

public class OnCommandsInitialized {
	public final CommandDispatcher< CommandSourceStack > dispatcher;

	public static Context< OnCommandsInitialized > listen( Consumer< OnCommandsInitialized > consumer ) {
		return Contexts.get( OnCommandsInitialized.class ).add( consumer );
	}

	public OnCommandsInitialized( CommandDispatcher< CommandSourceStack > dispatcher ) {
		this.dispatcher = dispatcher;
	}
}
