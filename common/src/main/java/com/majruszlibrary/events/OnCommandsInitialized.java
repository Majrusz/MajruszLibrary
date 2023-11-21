package com.majruszlibrary.events;

import com.majruszlibrary.events.base.Event;
import com.majruszlibrary.events.base.Events;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;

import java.util.function.Consumer;

public class OnCommandsInitialized {
	public final CommandDispatcher< CommandSourceStack > dispatcher;

	public static Event< OnCommandsInitialized > listen( Consumer< OnCommandsInitialized > consumer ) {
		return Events.get( OnCommandsInitialized.class ).add( consumer );
	}

	public OnCommandsInitialized( CommandDispatcher< CommandSourceStack > dispatcher ) {
		this.dispatcher = dispatcher;
	}
}
