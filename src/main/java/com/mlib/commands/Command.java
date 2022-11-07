package com.mlib.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.coordinates.Coordinates;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.RegisterCommandsEvent;

import java.util.ArrayList;
import java.util.Collection;
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

	protected Vec3 getPosition( CommandContext< CommandSourceStack > context ) {
		return this.getPosition( context, CommandBuilder.DefaultKeys.POSITION );
	}

	protected Vec3 getPosition( CommandContext< CommandSourceStack > context, String name ) {
		return context.getArgument( name, Coordinates.class ).getPosition( context.getSource() );
	}

	protected Entity getEntity( CommandContext< CommandSourceStack > context ) {
		return this.getEntity( context, CommandBuilder.DefaultKeys.ENTITY );
	}

	protected Entity getEntity( CommandContext< CommandSourceStack > context, String name ) {
		try {
			return context.getArgument( name, EntitySelector.class ).findSingleEntity( context.getSource() );
		} catch( Throwable exception ) {
			return null;
		}
	}

	protected Collection< ? extends Entity > getEntities( CommandContext< CommandSourceStack > context ) {
		return this.getEntities( context, CommandBuilder.DefaultKeys.ENTITIES );
	}

	protected Collection< ? extends Entity > getEntities( CommandContext< CommandSourceStack > context, String name ) {
		try {
			return context.getArgument( name, EntitySelector.class ).findEntities( context.getSource() );
		} catch( Throwable exception ) {
			return new ArrayList<>();
		}
	}

	protected void addBuilders( CommandBuilder... builders ) {
		for( CommandBuilder builder : builders ) {
			this.addBuilder( builder );
		}
	}

	protected void addBuilder( CommandBuilder builder ) {
		this.builders.add( builder );
	}

	private void register( CommandDispatcher< CommandSourceStack > dispatcher ) {
		this.builders.forEach( builder->builder.register( dispatcher ) );
	}
}
