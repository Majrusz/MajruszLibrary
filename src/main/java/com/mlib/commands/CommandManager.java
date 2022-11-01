package com.mlib.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;

import java.util.function.Predicate;

/** Manager for easier creating simple commands. (it was created only because Minecraft/Minecraft Forge interface often changes) */
@Deprecated
public class CommandManager {
	protected CommandDispatcher< CommandSourceStack > dispatcher;

	public CommandManager( CommandDispatcher< CommandSourceStack > dispatcher ) {
		this.dispatcher = dispatcher;
	}

	protected Command< CommandSourceStack > getCommand( IExecutable executable ) {
		return context->executable.execute( context, context.getSource() );
	}

	protected void register( LiteralArgumentBuilder< CommandSourceStack > builder ) {
		this.dispatcher.register( builder );
	}

	public void register( BaseCommand.Data data, IExecutable executable ) {
		LiteralArgumentBuilder< CommandSourceStack > commandBuilder = data.command;
		for( Predicate< CommandSourceStack > requirement : data.requirements )
			commandBuilder.requires( requirement );

		if( !data.arguments.isEmpty() ) {
			ArgumentBuilder< CommandSourceStack, ? > argumentBuilder = data.arguments.get( data.arguments.size() - 1 );
			argumentBuilder = argumentBuilder.executes( getCommand( executable ) );

			for( int i = data.arguments.size() - 2; i >= 0; --i ) {
				ArgumentBuilder< CommandSourceStack, ? > nextArgumentBuilder = data.arguments.get( i );
				argumentBuilder = nextArgumentBuilder.then( argumentBuilder );
			}

			this.dispatcher.register( commandBuilder.then( argumentBuilder ) );
		} else {
			this.dispatcher.register( commandBuilder.executes( getCommand( executable ) ) );
		}
	}

	/** Registers new command with two arguments. */
	public void register( LiteralArgumentBuilder< CommandSourceStack > argument1, ArgumentBuilder< CommandSourceStack, ? > argument2,
		ArgumentBuilder< CommandSourceStack, ? > argument3, IExecutable executable
	) {
		register( new BaseCommand.Data( argument1, argument2, argument3 ), executable );
	}

	/** Registers new command with two arguments and requirements. */
	public void register( LiteralArgumentBuilder< CommandSourceStack > argument1, ArgumentBuilder< CommandSourceStack, ? > argument2,
		ArgumentBuilder< CommandSourceStack, ? > argument3, Predicate< CommandSourceStack > requirement, IExecutable executable
	) {
		register( new BaseCommand.Data( requirement, argument1, argument2, argument3 ), executable );
	}

	/** Registers new command with one argument. */
	public void register( LiteralArgumentBuilder< CommandSourceStack > argument1, ArgumentBuilder< CommandSourceStack, ? > argument2,
		IExecutable executable
	) {
		register( new BaseCommand.Data( argument1, argument2 ), executable );
	}

	/** Registers new command with one argument and requirements. */
	public void register( LiteralArgumentBuilder< CommandSourceStack > argument1, ArgumentBuilder< CommandSourceStack, ? > argument2,
		Predicate< CommandSourceStack > requirement, IExecutable executable
	) {
		register( new BaseCommand.Data( requirement, argument1, argument2 ), executable );
	}

	/** Registers new command. */
	public void register( LiteralArgumentBuilder< CommandSourceStack > argument, IExecutable executable ) {
		register( new BaseCommand.Data( argument ), executable );
	}

	/** Registers new command with requirements. */
	public void register( LiteralArgumentBuilder< CommandSourceStack > argument, Predicate< CommandSourceStack > requirement, IExecutable executable
	) {
		register( new BaseCommand.Data( requirement, argument ), executable );
	}

	@FunctionalInterface
	public interface IExecutable {
		int execute( CommandContext< CommandSourceStack > context, CommandSourceStack source );
	}
}
