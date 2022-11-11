package com.mlib.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.coordinates.Coordinates;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.RegisterCommandsEvent;

import java.util.*;

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
		this.addBuilder( builder );

		return builder;
	}

	protected int getInteger( CommandContext< CommandSourceStack > context ) {
		return this.getInteger( context, CommandBuilder.DefaultKeys.INT );
	}

	protected int getInteger( CommandContext< CommandSourceStack > context, String name ) {
		return context.getArgument( name, int.class );
	}

	protected Optional< Integer > getOptionalInteger( CommandContext< CommandSourceStack > context ) {
		try {
			return Optional.of( this.getInteger( context ) );
		} catch( Throwable exception ) {
			return Optional.empty();
		}
	}

	protected Optional< Integer > getOptionalInteger( CommandContext< CommandSourceStack > context, String name ) {
		try {
			return Optional.of( this.getInteger( context, name ) );
		} catch( Throwable exception ) {
			return Optional.empty();
		}
	}

	protected < EnumType extends Enum< EnumType > > EnumType getEnumeration( CommandContext< CommandSourceStack > context, Class< EnumType > enumClass ) {
		return this.getEnumeration( context, CommandBuilder.DefaultKeys.ENUM, enumClass );
	}

	protected < EnumType extends Enum< EnumType > > EnumType getEnumeration( CommandContext< CommandSourceStack > context, String name,
		Class< EnumType > enumClass
	) {
		return context.getArgument( name, enumClass );
	}

	protected < EnumType extends Enum< EnumType > > Optional< EnumType > getOptionalEnumeration( CommandContext< CommandSourceStack > context,
		Class< EnumType > enumClass
	) {
		try {
			return Optional.of( this.getEnumeration( context, enumClass ) );
		} catch( Throwable exception ) {
			return Optional.empty();
		}
	}

	protected < EnumType extends Enum< EnumType > > Optional< EnumType > getOptionalEnumeration( CommandContext< CommandSourceStack > context, String name,
		Class< EnumType > enumClass
	) {
		try {
			return Optional.of( this.getEnumeration( context, name, enumClass ) );
		} catch( Throwable exception ) {
			return Optional.empty();
		}
	}

	protected Vec3 getPosition( CommandContext< CommandSourceStack > context ) {
		return this.getPosition( context, CommandBuilder.DefaultKeys.POSITION );
	}

	protected Vec3 getPosition( CommandContext< CommandSourceStack > context, String name ) {
		return context.getArgument( name, Coordinates.class ).getPosition( context.getSource() );
	}

	protected Optional< Vec3 > getOptionalPosition( CommandContext< CommandSourceStack > context ) {
		try {
			return Optional.of( this.getPosition( context ) );
		} catch( Throwable exception ) {
			return Optional.empty();
		}
	}

	protected Optional< Vec3 > getOptionalPosition( CommandContext< CommandSourceStack > context, String name ) {
		try {
			return Optional.of( this.getPosition( context, name ) );
		} catch( Throwable exception ) {
			return Optional.empty();
		}
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

	protected Optional< Entity > getOptionalEntity( CommandContext< CommandSourceStack > context ) {
		try {
			return Optional.of( this.getEntity( context ) );
		} catch( Throwable exception ) {
			return Optional.empty();
		}
	}

	protected Optional< Entity > getOptionalEntity( CommandContext< CommandSourceStack > context, String name ) {
		try {
			return Optional.of( this.getEntity( context, name ) );
		} catch( Throwable exception ) {
			return Optional.empty();
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

	protected List< Vec3 > getAnyPositions( CommandContext< CommandSourceStack > context ) {
		List< Vec3 > positions = new ArrayList<>();
		try {
			positions.add( this.getPosition( context ) );
		} catch( Throwable ignored ) {}
		try {
			positions.add( this.getEntity( context ).position() );
		} catch( Throwable ignored ) {}
		try {
			this.getEntities( context ).forEach( entity->positions.add( entity.position() ) );
		} catch( Throwable ignored ) {}

		return positions;
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
