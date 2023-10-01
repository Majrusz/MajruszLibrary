package com.mlib.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.coordinates.Coordinates;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class CommandData {
	public final CommandContext< CommandSourceStack > context;
	public final CommandSourceStack source;

	public CommandData( CommandContext< CommandSourceStack > context ) {
		this.context = context;
		this.source = context.getSource();
	}

	public int getInteger() {
		return this.getInteger( Command.DefaultKeys.INT );
	}

	public int getInteger( String name ) {
		return this.context.getArgument( name, int.class );
	}

	public Optional< Integer > getOptionalInteger() {
		try {
			return Optional.of( this.getInteger() );
		} catch( Throwable exception ) {
			return Optional.empty();
		}
	}

	public Optional< Integer > getOptionalInteger( String name ) {
		try {
			return Optional.of( this.getInteger( name ) );
		} catch( Throwable exception ) {
			return Optional.empty();
		}
	}

	public < EnumType extends Enum< EnumType > > EnumType getEnumeration( Class< EnumType > enumClass ) {
		return this.getEnumeration( enumClass.getSimpleName().toLowerCase(), enumClass );
	}

	public < EnumType extends Enum< EnumType > > EnumType getEnumeration( String name, Class< EnumType > enumClass ) {
		return this.context.getArgument( name, enumClass );
	}

	public < EnumType extends Enum< EnumType > > Optional< EnumType > getOptionalEnumeration( Class< EnumType > enumClass ) {
		try {
			return Optional.of( this.getEnumeration( enumClass ) );
		} catch( Throwable exception ) {
			return Optional.empty();
		}
	}

	public < EnumType extends Enum< EnumType > > Optional< EnumType > getOptionalEnumeration( String name, Class< EnumType > enumClass ) {
		try {
			return Optional.of( this.getEnumeration( name, enumClass ) );
		} catch( Throwable exception ) {
			return Optional.empty();
		}
	}

	public Vec3 getPosition() {
		return this.getPosition( Command.DefaultKeys.POSITION );
	}

	public Vec3 getPosition( String name ) {
		return this.context.getArgument( name, Coordinates.class ).getPosition( this.context.getSource() );
	}

	public Optional< Vec3 > getOptionalPosition( CommandData data ) {
		try {
			return Optional.of( this.getPosition() );
		} catch( Throwable exception ) {
			return Optional.empty();
		}
	}

	public Optional< Vec3 > getOptionalPosition( String name ) {
		try {
			return Optional.of( this.getPosition( name ) );
		} catch( Throwable exception ) {
			return Optional.empty();
		}
	}

	public Entity getEntity() {
		return this.getEntity( Command.DefaultKeys.ENTITY );
	}

	public Entity getEntity( String name ) {
		try {
			return this.context.getArgument( name, EntitySelector.class ).findSingleEntity( this.context.getSource() );
		} catch( Throwable exception ) {
			return null;
		}
	}

	public Optional< Entity > getOptionalEntity() {
		try {
			return Optional.of( this.getEntity() );
		} catch( Throwable exception ) {
			return Optional.empty();
		}
	}

	public Optional< Entity > getOptionalEntity( String name ) {
		try {
			return Optional.of( this.getEntity( name ) );
		} catch( Throwable exception ) {
			return Optional.empty();
		}
	}

	public Entity getOptionalEntityOrPlayer() throws CommandSyntaxException {
		Optional< Entity > entity = this.getOptionalEntity();

		return entity.isPresent() ? entity.get() : this.source.getEntityOrException();
	}

	public Collection< ? extends Entity > getEntities() {
		return this.getEntities( Command.DefaultKeys.ENTITIES );
	}

	public Collection< ? extends Entity > getEntities( String name ) {
		try {
			return this.context.getArgument( name, EntitySelector.class ).findEntities( this.context.getSource() );
		} catch( Throwable exception ) {
			return new ArrayList<>();
		}
	}

	public List< Vec3 > getAnyPositions( CommandData data ) {
		List< Vec3 > positions = new ArrayList<>();
		try {
			positions.add( this.getPosition() );
		} catch( Throwable ignored ) {}
		try {
			positions.add( this.getEntity().position() );
		} catch( Throwable ignored ) {}
		try {
			this.getEntities().forEach( entity->positions.add( entity.position() ) );
		} catch( Throwable ignored ) {}

		return positions;
	}
}
