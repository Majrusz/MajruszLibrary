package com.mlib.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;

import java.util.function.Predicate;

/** Manager for easier creating simple commands. (it was created only because Minecraft/Minecraft Forge interface often changes) */
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

	/** Registers new command with two arguments. */
	public void registerCommand( LiteralArgumentBuilder< CommandSourceStack > argument1, ArgumentBuilder< CommandSourceStack, ? > argument2, ArgumentBuilder< CommandSourceStack, ? > argument3, IExecutable executable ) {
		register( argument1.then( argument2.then( argument3.executes( getCommand( executable ) ) ) ) );
	}

	/** Registers new command with two arguments and requirements. */
	public void registerCommand( LiteralArgumentBuilder< CommandSourceStack > argument1, ArgumentBuilder< CommandSourceStack, ? > argument2, ArgumentBuilder< CommandSourceStack, ? > argument3, Predicate< CommandSourceStack > requirement, IExecutable executable ) {
		register( argument1.requires( requirement ).then( argument2.then( argument3.executes( getCommand( executable ) ) ) ) );
	}

	/** Registers new command with one argument. */
	public void registerCommand( LiteralArgumentBuilder< CommandSourceStack > argument1, ArgumentBuilder< CommandSourceStack, ? > argument2, IExecutable executable ) {
		register( argument1.then( argument2.executes( getCommand( executable ) ) ) );
	}

	/** Registers new command with one argument and requirements. */
	public void registerCommand( LiteralArgumentBuilder< CommandSourceStack > argument1, ArgumentBuilder< CommandSourceStack, ? > argument2, Predicate< CommandSourceStack > requirement, IExecutable executable ) {
		register( argument1.requires( requirement ).then( argument2.executes( getCommand( executable ) ) ) );
	}

	/** Registers new command with requirements. */
	public void registerCommand( LiteralArgumentBuilder< CommandSourceStack > argument, Predicate< CommandSourceStack > requirement, IExecutable executable ) {
		register( argument.requires( requirement ).executes( getCommand( executable ) ) );
	}

	/** Registers new command. */
	public void registerCommand( LiteralArgumentBuilder< CommandSourceStack > argument, IExecutable executable ) {
		register( argument.executes( getCommand( executable ) ) );
	}

	@FunctionalInterface
	public interface IExecutable {
		int execute( CommandContext< CommandSourceStack > context, CommandSourceStack source );
	}
}
