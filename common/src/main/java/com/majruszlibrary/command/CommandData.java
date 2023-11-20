package com.majruszlibrary.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.entity.Entity;

import java.util.Optional;

public class CommandData {
	public final CommandContext< CommandSourceStack > context;
	public final CommandSourceStack source;

	public CommandData( CommandContext< CommandSourceStack > context ) {
		this.context = context;
		this.source = context.getSource();
	}

	public < Type > Type get( IParameter< Type > parameter ) throws CommandSyntaxException {
		return parameter.get( this.context );
	}

	public < Type > Optional< Type > getOptional( IParameter< Type > parameter ) {
		try {
			return Optional.of( parameter.get( this.context ) );
		} catch( Throwable exception ) {
			return Optional.empty();
		}
	}

	public Entity getCaller() {
		try {
			return this.source.getEntityOrException();
		} catch( Throwable exception ) {
			return null;
		}
	}
}
