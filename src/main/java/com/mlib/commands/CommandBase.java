package com.mlib.commands;

import com.mojang.brigadier.CommandDispatcher;
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
import net.minecraftforge.event.RegisterCommandsEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

/** Base class for easier creating simple commands. */
public abstract class CommandBase {
	private static final String DEFAULT_LOCATION_ARGUMENT = "location";
	private static final String DEFAULT_ENTITY_ARGUMENT = "entity";
	private static final String DEFAULT_ENTITIES_ARGUMENT = "entities";
	private static final List< CommandBase > COMMANDS = new ArrayList<>();

	public CommandBase() {
		COMMANDS.add( this );
	}

	/** Registers all created commands. (should be called only be Majrusz Library!) */
	public static void registerAll( RegisterCommandsEvent event ) {
		for( CommandBase command : COMMANDS )
			command.register( event.getDispatcher() );
	}

	public static Predicate< CommandSourceStack > hasPermission( int requiredLevel ) {
		return commandSourceStack->commandSourceStack.hasPermission( requiredLevel );
	}

	/** Adds text argument. */
	public static LiteralArgumentBuilder< CommandSourceStack > literal( String name ) {
		return Commands.literal( name );
	}

	/** Adds location argument. */
	public static RequiredArgumentBuilder< CommandSourceStack, Coordinates > location( String name ) {
		return Commands.argument( name, Vec3Argument.vec3() );
	}

	/** Returns location argument. */
	public static Vec3 getLocation( CommandContext< CommandSourceStack > context, String name ) {
		return Vec3Argument.getVec3( context, name );
	}

	/** Adds location argument. */
	public static RequiredArgumentBuilder< CommandSourceStack, Coordinates > location() {
		return location( DEFAULT_LOCATION_ARGUMENT );
	}

	/** Returns location argument. */
	public static Vec3 getLocation( CommandContext< CommandSourceStack > context ) {
		return getLocation( context, DEFAULT_LOCATION_ARGUMENT );
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

	/** Registers this command. */
	public abstract void register( CommandDispatcher< CommandSourceStack > dispatcher );
}
