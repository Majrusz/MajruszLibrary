package com.mlib.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraftforge.event.RegisterCommandsEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Command {
	static final List< Command > COMMANDS = Collections.synchronizedList( new ArrayList<>() );
	final List< CommandBuilder > builders = new ArrayList<>();

	public static void registerAll( RegisterCommandsEvent event ) {
		COMMANDS.forEach( command->command.register( event.getDispatcher() ) );
	}

	public Command() {
		COMMANDS.add( this );
	}

	protected CommandBuilder newBuilder() {
		CommandBuilder builder = new CommandBuilder();
		this.builders.add( builder );

		return builder;
	}

	protected int getInteger( CommandContext< CommandSourceStack > context ) {
		return this.getInteger( context, CommandBuilder.DefaultKeys.INT );
	}

	protected int getInteger( CommandContext< CommandSourceStack > context, String name ) {
		return context.getArgument( name, int.class );
	}

	protected < EnumType extends Enum< EnumType > > EnumType getEnumeration( CommandContext< CommandSourceStack > context, Class< EnumType > enumClass ) {
		return this.getEnumeration( context, CommandBuilder.DefaultKeys.ENUM, enumClass );
	}

	protected < EnumType extends Enum< EnumType > > EnumType getEnumeration( CommandContext< CommandSourceStack > context, String name,
		Class< EnumType > enumClass
	) {
		return context.getArgument( name, enumClass );
	}

	private void register( CommandDispatcher< CommandSourceStack > dispatcher ) {
		this.builders.forEach( builder->builder.register( dispatcher ) );
	}
}
