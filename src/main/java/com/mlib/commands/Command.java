package com.mlib.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
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

	protected int getInteger( CommandData data ) {
		return this.getInteger( data, CommandBuilder.DefaultKeys.INT );
	}

	protected int getInteger( CommandData data, String name ) {
		return data.context.getArgument( name, int.class );
	}

	protected Optional< Integer > getOptionalInteger( CommandData data ) {
		try {
			return Optional.of( this.getInteger( data ) );
		} catch( Throwable exception ) {
			return Optional.empty();
		}
	}

	protected Optional< Integer > getOptionalInteger( CommandData data, String name ) {
		try {
			return Optional.of( this.getInteger( data, name ) );
		} catch( Throwable exception ) {
			return Optional.empty();
		}
	}

	protected < EnumType extends Enum< EnumType > > EnumType getEnumeration( CommandData data, Class< EnumType > enumClass ) {
		return this.getEnumeration( data, enumClass.getSimpleName().toLowerCase(), enumClass );
	}

	protected < EnumType extends Enum< EnumType > > EnumType getEnumeration( CommandData data, String name,
		Class< EnumType > enumClass
	) {
		return data.context.getArgument( name, enumClass );
	}

	protected < EnumType extends Enum< EnumType > > Optional< EnumType > getOptionalEnumeration( CommandData data,
		Class< EnumType > enumClass
	) {
		try {
			return Optional.of( this.getEnumeration( data, enumClass ) );
		} catch( Throwable exception ) {
			return Optional.empty();
		}
	}

	protected < EnumType extends Enum< EnumType > > Optional< EnumType > getOptionalEnumeration( CommandData data, String name,
		Class< EnumType > enumClass
	) {
		try {
			return Optional.of( this.getEnumeration( data, name, enumClass ) );
		} catch( Throwable exception ) {
			return Optional.empty();
		}
	}

	protected Vec3 getPosition( CommandData data ) {
		return this.getPosition( data, CommandBuilder.DefaultKeys.POSITION );
	}

	protected Vec3 getPosition( CommandData data, String name ) {
		return data.context.getArgument( name, Coordinates.class ).getPosition( data.context.getSource() );
	}

	protected Optional< Vec3 > getOptionalPosition( CommandData data ) {
		try {
			return Optional.of( this.getPosition( data ) );
		} catch( Throwable exception ) {
			return Optional.empty();
		}
	}

	protected Optional< Vec3 > getOptionalPosition( CommandData data, String name ) {
		try {
			return Optional.of( this.getPosition( data, name ) );
		} catch( Throwable exception ) {
			return Optional.empty();
		}
	}

	protected Entity getEntity( CommandData data ) {
		return this.getEntity( data, CommandBuilder.DefaultKeys.ENTITY );
	}

	protected Entity getEntity( CommandData data, String name ) {
		try {
			return data.context.getArgument( name, EntitySelector.class ).findSingleEntity( data.context.getSource() );
		} catch( Throwable exception ) {
			return null;
		}
	}

	protected Optional< Entity > getOptionalEntity( CommandData data ) {
		try {
			return Optional.of( this.getEntity( data ) );
		} catch( Throwable exception ) {
			return Optional.empty();
		}
	}

	protected Optional< Entity > getOptionalEntity( CommandData data, String name ) {
		try {
			return Optional.of( this.getEntity( data, name ) );
		} catch( Throwable exception ) {
			return Optional.empty();
		}
	}

	protected Entity getOptionalEntityOrPlayer( CommandData data ) throws CommandSyntaxException {
		Optional< Entity > entity = this.getOptionalEntity( data );
		if( entity.isPresent() )
			return entity.get();

		return data.source.getEntityOrException();
	}

	protected Collection< ? extends Entity > getEntities( CommandData data ) {
		return this.getEntities( data, CommandBuilder.DefaultKeys.ENTITIES );
	}

	protected Collection< ? extends Entity > getEntities( CommandData data, String name ) {
		try {
			return data.context.getArgument( name, EntitySelector.class ).findEntities( data.context.getSource() );
		} catch( Throwable exception ) {
			return new ArrayList<>();
		}
	}

	protected List< Vec3 > getAnyPositions( CommandData data ) {
		List< Vec3 > positions = new ArrayList<>();
		try {
			positions.add( this.getPosition( data ) );
		} catch( Throwable ignored ) {}
		try {
			positions.add( this.getEntity( data ).position() );
		} catch( Throwable ignored ) {}
		try {
			this.getEntities( data ).forEach( entity->positions.add( entity.position() ) );
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
