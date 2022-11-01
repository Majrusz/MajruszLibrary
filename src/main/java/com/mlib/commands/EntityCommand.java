package com.mlib.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.entity.Entity;

/** For easier creating entity commands. (2 similar commands with arguments: one entity or entity list) */
@Deprecated
public abstract class EntityCommand extends BaseCommand {
	protected void registerEntityCommand( CommandDispatcher< CommandSourceStack > commandDispatcher, Data data ) {
		CommandManager commandManager = new CommandManager( commandDispatcher );
		commandManager.register( data.extendCopy( entity() ), this::handleEntityArgument );
		commandManager.register( data.extendCopy( entities() ), this::handleEntitiesArgument );
		commandManager.register( data, this::handleNoArgument );
	}

	protected int handleEntityArgument( CommandContext< CommandSourceStack > context, CommandSourceStack source ) {
		return handleCommand( context, source, getEntity( context ) );
	}

	protected int handleEntitiesArgument( CommandContext< CommandSourceStack > context, CommandSourceStack source ) {
		int result = -1;
		for( Entity entity : getEntities( context ) )
			result = Math.max( result, handleCommand( context, source, entity ) );

		return result;
	}

	protected int handleNoArgument( CommandContext< CommandSourceStack > context, CommandSourceStack source ) {
		return handleCommand( context, source, source.getEntity() );
	}

	protected abstract int handleCommand( CommandContext< CommandSourceStack > context, CommandSourceStack source, Entity entity );
}
