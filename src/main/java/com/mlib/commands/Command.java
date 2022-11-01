package com.mlib.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraftforge.event.RegisterCommandsEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Command {
	static final List< Command > COMMANDS = Collections.synchronizedList( new ArrayList<>() );

	public Command() {
		COMMANDS.add( this );
	}

	public static void registerCommands( RegisterCommandsEvent event ) {
		COMMANDS.forEach( command->command.register( event.getDispatcher() ) );
	}

	private void register( CommandDispatcher< CommandSourceStack > dispatcher ) {

	}
}
