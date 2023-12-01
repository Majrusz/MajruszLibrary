package com.majruszlibrary.command;

import com.majruszlibrary.math.Range;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.commands.arguments.coordinates.Coordinates;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class Command {
	public static CommandBuilder create() {
		return new CommandBuilder();
	}

	private Command() {}

	public static IParameter.Named< Integer > integer() {
		return Command.integer( null );
	}

	public static IParameter.Named< Integer > integer( Range< Integer > range ) {
		IParameter.Named< Integer > parameter = new IParameter.Named<>();
		IntegerArgumentType argument;
		if( range == null ) {
			argument = IntegerArgumentType.integer();
		} else if( range.to == null ) {
			argument = IntegerArgumentType.integer( range.from );
		} else {
			argument = IntegerArgumentType.integer( range.from, range.to );
		}

		return parameter.applier( builder->builder.addArgument( ()->Commands.argument( parameter.name, argument ) ) )
			.getter( context->context.getArgument( parameter.name, Integer.class ) )
			.named( DefaultKeys.VALUE );
	}

	public static IParameter.Named< Float > number() {
		return Command.number( null );
	}

	public static IParameter.Named< Float > number( Range< Float > range ) {
		IParameter.Named< Float > parameter = new IParameter.Named<>();
		FloatArgumentType argument;
		if( range == null ) {
			argument = FloatArgumentType.floatArg();
		} else if( range.to == null ) {
			argument = FloatArgumentType.floatArg( range.from );
		} else {
			argument = FloatArgumentType.floatArg( range.from, range.to );
		}

		return parameter.applier( builder->builder.addArgument( ()->Commands.argument( parameter.name, argument ) ) )
			.getter( context->context.getArgument( parameter.name, Float.class ) )
			.named( DefaultKeys.VALUE );
	}

	public static IParameter.Named< Boolean > bool() {
		IParameter.Named< Boolean > parameter = new IParameter.Named<>();

		return parameter.applier( builder->builder.addArgument( ()->Commands.argument( parameter.name, BoolArgumentType.bool() ) ) )
			.getter( context->context.getArgument( parameter.name, boolean.class ) )
			.named( DefaultKeys.VALUE );
	}

	public static IParameter.Hinted< String > string() {
		IParameter.Hinted< String > parameter = new IParameter.Hinted<>();
		parameter.applier( builder->builder.addArgument( ()->Commands.argument( parameter.name, StringArgumentType.string() )
				.suggests( ( context, subbuilder )->SharedSuggestionProvider.suggest( parameter.suggestions.get(), subbuilder ) ) )
			)
			.getter( context->context.getArgument( parameter.name, String.class ) )
			.named( DefaultKeys.VALUE );

		return parameter;
	}

	public static IParameter.Hinted< ResourceLocation > resource() {
		IParameter.Hinted< ResourceLocation > parameter = new IParameter.Hinted<>();
		parameter.applier( builder->builder.addArgument( ()->Commands.argument( parameter.name, ResourceLocationArgument.id() )
				.suggests( ( context, subbuilder )->SharedSuggestionProvider.suggestResource( parameter.suggestions.get(), subbuilder ) ) )
			)
			.getter( context->context.getArgument( parameter.name, ResourceLocation.class ) )
			.named( DefaultKeys.VALUE );

		return parameter;
	}

	public static < EnumType extends Enum< EnumType > > IParameter.Named< EnumType > enumeration( Supplier< EnumType[] > values ) {
		IParameter.Named< EnumType > parameter = new IParameter.Named<>();

		return parameter.applier( builder->builder.addArgument( ()->Commands.argument( parameter.name, StringArgumentType.string() )
				.suggests( ( context, subbuilder )->SharedSuggestionProvider.suggest( Arrays.stream( values.get() ).map( Enum::name ).toList(), subbuilder ) ) )
			)
			.getter( context->{
				String name = context.getArgument( parameter.name, String.class );
				for( EnumType value : values.get() ) {
					if( name.equals( value.name() ) ) {
						return value;
					}
				}

				throw new IllegalStateException();
			} )
			.named( DefaultKeys.VALUE );
	}

	public static IParameter.Named< Vec3 > position() {
		IParameter.Named< Vec3 > parameter = new IParameter.Named<>();

		return parameter.applier( builder->builder.addArgument( ()->Commands.argument( parameter.name, Vec3Argument.vec3() ) ) )
			.getter( context->context.getArgument( parameter.name, Coordinates.class ).getPosition( context.getSource() ) )
			.named( DefaultKeys.POSITION );
	}

	public static IParameter.Named< List< ? extends Entity > > entities() {
		IParameter.Named< List< ? extends Entity > > parameter = new IParameter.Named<>();

		return parameter.applier( builder->builder.addArgument( ()->Commands.argument( parameter.name, EntityArgument.entities() ) ) )
			.getter( context->{
				try {
					return context.getArgument( parameter.name, EntitySelector.class ).findEntities( context.getSource() );
				} catch( Throwable exception ) {
					return null;
				}
			} )
			.named( DefaultKeys.ENTITIES );
	}

	public static IParameter< List< Vec3 > > anyPosition() {
		IParameter.Named< List< Vec3 > > parameter = new IParameter.Named<>();

		return parameter.applier( builder->{
				builder.add( List.of(
					subbuilder->subbuilder.addArgument( Commands.argument( DefaultKeys.POSITION, Vec3Argument.vec3() ) ),
					subbuilder->subbuilder.addArgument( Commands.argument( DefaultKeys.ENTITIES, EntityArgument.entities() ) )
				) );
			} )
			.getter( context->{
				List< Vec3 > positions = new ArrayList<>();
				try {
					positions.add( context.getArgument( DefaultKeys.POSITION, Coordinates.class ).getPosition( context.getSource() ) );
				} catch( Throwable ignored ) {}
				try {
					context.getArgument( DefaultKeys.ENTITIES, EntitySelector.class )
						.findEntities( context.getSource() )
						.forEach( entity->positions.add( entity.position() ) );
				} catch( Throwable ignored ) {}

				return positions;
			} );
	}

	@FunctionalInterface
	public interface IModification {
		void apply( CommandBuilder builder );
	}

	@FunctionalInterface
	public interface IExecutable {
		int execute( CommandData data ) throws CommandSyntaxException;
	}

	public static class DefaultKeys {
		public static final String ENTITIES = "entities";
		public static final String VALUE = "value";
		public static final String POSITION = "position";
	}
}
