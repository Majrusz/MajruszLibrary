package com.mlib.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

/** For easier creating position commands. (4 similar commands with arguments: location, entity, entities or source location) */
public abstract class PositionCommand extends BaseCommand {
	protected void registerLocationCommand( CommandDispatcher< CommandSourceStack > commandDispatcher, Data data ) {
		CommandManager commandManager = new CommandManager( commandDispatcher );
		commandManager.register( data.extendCopy( position() ), this::handleLocationArgument );
		commandManager.register( data.extendCopy( entity() ), this::handleEntityArgument );
		commandManager.register( data.extendCopy( entities() ), this::handleEntitiesArgument );
		commandManager.register( data, this::handleNoArgument );
	}

	protected int handleLocationArgument( CommandContext< CommandSourceStack > context, CommandSourceStack source ) {
		return handleCommand( context, source, getPosition( context ) );
	}

	protected int handleEntityArgument( CommandContext< CommandSourceStack > context, CommandSourceStack source ) {
		return handleCommand( context, source, getEntity( context ).position() );
	}

	protected int handleEntitiesArgument( CommandContext< CommandSourceStack > context, CommandSourceStack source ) {
		int result = -1;
		for( Entity entity : getEntities( context ) )
			result = Math.max( result, handleCommand( context, source, entity.position() ) );

		return result;
	}

	protected int handleNoArgument( CommandContext< CommandSourceStack > context, CommandSourceStack source ) {
		return handleCommand( context, source, source.getPosition() );
	}

	protected abstract int handleCommand( CommandContext< CommandSourceStack > context, CommandSourceStack source, Vec3 vec3 );
}
