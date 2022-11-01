package com.mlib.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.coordinates.Coordinates;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.server.command.EnumArgument;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;

/** Base class for easier creating simple commands. */
@Deprecated
public class BaseCommand {
	private static final String DEFAULT_POSITION_ARGUMENT = "position";
	private static final String DEFAULT_ENTITY_ARGUMENT = "entity";
	private static final String DEFAULT_ENTITIES_ARGUMENT = "entities";

	public static Predicate< CommandSourceStack > hasPermission( int requiredLevel ) {
		return commandSourceStack->commandSourceStack.hasPermission( requiredLevel );
	}

	/** Adds integer argument. */
	public static RequiredArgumentBuilder< CommandSourceStack, Integer > integer( String name ) {
		return Commands.argument( name, IntegerArgumentType.integer() );
	}

	/** Adds integer argument. */
	public static RequiredArgumentBuilder< CommandSourceStack, Integer > integer( String name, int min ) {
		return Commands.argument( name, IntegerArgumentType.integer( min ) );
	}

	/** Adds integer argument. */
	public static RequiredArgumentBuilder< CommandSourceStack, Integer > integer( String name, int min, int max ) {
		return Commands.argument( name, IntegerArgumentType.integer( min, max ) );
	}

	/** Returns integer argument. */
	public static int getInteger( CommandContext< CommandSourceStack > context, String name ) {
		return IntegerArgumentType.getInteger( context, name );
	}

	/** Adds enum argument. */
	public static < EnumType extends Enum< EnumType > > RequiredArgumentBuilder< CommandSourceStack, EnumType > enumArgument( String name,
		Class< EnumType > enumClass
	) {
		return Commands.argument( name, EnumArgument.enumArgument( enumClass ) );
	}

	/** Returns enum argument. */
	public static < EnumType extends Enum< EnumType > > EnumType getEnum( CommandContext< CommandSourceStack > context, String name,
		Class< EnumType > enumClass
	) {
		return context.getArgument( name, enumClass );
	}

	/** Returns enum argument if it was given. */
	public static < EnumType extends Enum< EnumType > > Optional< EnumType > getOptionalEnum( CommandContext< CommandSourceStack > context, String name,
		Class< EnumType > enumClass
	) {
		try {
			return Optional.of( context.getArgument( name, enumClass ) );
		} catch( IllegalArgumentException exception ) {
			return Optional.empty();
		}
	}

	/** Adds text argument. */
	public static LiteralArgumentBuilder< CommandSourceStack > literal( String name ) {
		return Commands.literal( name );
	}

	/** Adds position argument. */
	public static RequiredArgumentBuilder< CommandSourceStack, Coordinates > position( String name ) {
		return Commands.argument( name, Vec3Argument.vec3() );
	}

	/** Returns position argument. */
	public static Vec3 getPosition( CommandContext< CommandSourceStack > context, String name ) {
		return Vec3Argument.getVec3( context, name );
	}

	/** Returns position argument if it was given. */
	public static Optional< Vec3 > getOptionalPosition( CommandContext< CommandSourceStack > context, String name ) {
		try {
			return Optional.of( getPosition( context, name ) );
		} catch( IllegalArgumentException exception ) {
			return Optional.empty();
		}
	}

	/** Adds position argument. */
	public static RequiredArgumentBuilder< CommandSourceStack, Coordinates > position() {
		return position( DEFAULT_POSITION_ARGUMENT );
	}

	/** Returns position argument. */
	public static Vec3 getPosition( CommandContext< CommandSourceStack > context ) {
		return getPosition( context, DEFAULT_POSITION_ARGUMENT );
	}

	/** Returns position argument if it was given. */
	public static Optional< Vec3 > getOptionalPosition( CommandContext< CommandSourceStack > context ) {
		try {
			return Optional.of( getPosition( context ) );
		} catch( IllegalArgumentException exception ) {
			return Optional.empty();
		}
	}

	/** Adds entity argument. */
	public static RequiredArgumentBuilder< CommandSourceStack, EntitySelector > entity( String name ) {
		return Commands.argument( name, EntityArgument.entity() );
	}

	/** Returns entity argument. */
	@Nullable
	public static Entity getEntity( CommandContext< CommandSourceStack > context, String name ) {
		try {
			return EntityArgument.getEntity( context, name );
		} catch( CommandSyntaxException exception ) {
			return null;
		}
	}

	/** Adds entity argument. */
	public static RequiredArgumentBuilder< CommandSourceStack, EntitySelector > entity() {
		return entity( DEFAULT_ENTITY_ARGUMENT );
	}

	/** Returns entity argument. */
	public static Entity getEntity( CommandContext< CommandSourceStack > context ) {
		return getEntity( context, DEFAULT_ENTITY_ARGUMENT );
	}

	/** Adds entities argument. */
	public static RequiredArgumentBuilder< CommandSourceStack, EntitySelector > entities( String name ) {
		return Commands.argument( name, EntityArgument.entities() );
	}

	/** Returns entities argument. */
	@Nullable
	public static Collection< ? extends Entity > getEntities( CommandContext< CommandSourceStack > context, String name ) {
		try {
			return EntityArgument.getEntities( context, name );
		} catch( CommandSyntaxException exception ) {
			return null;
		}
	}

	/** Adds entities argument. */
	public static RequiredArgumentBuilder< CommandSourceStack, EntitySelector > entities() {
		return entities( DEFAULT_ENTITIES_ARGUMENT );
	}

	/** Returns entities argument. */
	public static Collection< ? extends Entity > getEntities( CommandContext< CommandSourceStack > context ) {
		return getEntities( context, DEFAULT_ENTITIES_ARGUMENT );
	}

	/** Stores information about command. */
	public static class Data {
		public final List< ArgumentBuilder< CommandSourceStack, ? > > arguments = new ArrayList<>();
		public final List< Predicate< CommandSourceStack > > requirements = new ArrayList<>();
		public final LiteralArgumentBuilder< CommandSourceStack > command;

		@SafeVarargs
		public Data( LiteralArgumentBuilder< CommandSourceStack > command, ArgumentBuilder< CommandSourceStack, ? >... arguments ) {
			this.command = command;
			addArguments( arguments );
		}

		@SafeVarargs
		public Data( Predicate< CommandSourceStack > requirement, LiteralArgumentBuilder< CommandSourceStack > command,
			ArgumentBuilder< CommandSourceStack, ? >... arguments
		) {
			this( command, arguments );
			addRequirements( requirement );
		}

		@SafeVarargs
		public final void addArguments( ArgumentBuilder< CommandSourceStack, ? >... arguments ) {
			Collections.addAll( this.arguments, arguments );
		}

		@SafeVarargs
		public final void addRequirements( Predicate< CommandSourceStack >... requirements ) {
			Collections.addAll( this.requirements, requirements );
		}

		public Data copy() {
			Data dataCopy = new Data( this.command );
			dataCopy.arguments.addAll( this.arguments );
			dataCopy.requirements.addAll( this.requirements );

			return dataCopy;
		}

		@SafeVarargs
		public final Data extendCopy( ArgumentBuilder< CommandSourceStack, ? >... arguments ) {
			Data dataCopy = copy();
			dataCopy.addArguments( arguments );

			return dataCopy;
		}
	}
}
