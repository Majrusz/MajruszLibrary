package com.mlib.commands;

import com.mlib.math.Range;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
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

import java.util.ArrayList;
import java.util.List;

public class Command {
	public static CommandBuilder create() {
		return new CommandBuilder();
	}

	private Command() {}

	public static IParameter.Named< Integer > integer() {
		return Command.integer( null );
	}

	public static IParameter.Named< Integer > integer( Range< Integer > range ) {
		IntegerArgumentType argument;
		if( range == null ) {
			argument = IntegerArgumentType.integer();
		} else if( range.to == null ) {
			argument = IntegerArgumentType.integer( range.from );
		} else {
			argument = IntegerArgumentType.integer( range.from, range.to );
		}

		return new IParameter.Named< Integer >() {
			@Override
			public CommandBuilder apply( CommandBuilder builder ) {
				return builder.addArgument( ()->Commands.argument( this.name, argument ) );
			}

			@Override
			public Integer get( CommandContext< CommandSourceStack > context ) {
				return context.getArgument( this.name, Integer.class );
			}
		}.named( DefaultKeys.VALUE );
	}

	public static IParameter.Named< Float > number() {
		return Command.number( null );
	}

	public static IParameter.Named< Float > number( Range< Float > range ) {
		FloatArgumentType argument;
		if( range == null ) {
			argument = FloatArgumentType.floatArg();
		} else if( range.to == null ) {
			argument = FloatArgumentType.floatArg( range.from );
		} else {
			argument = FloatArgumentType.floatArg( range.from, range.to );
		}

		return new IParameter.Named< Float >() {
			@Override
			public CommandBuilder apply( CommandBuilder builder ) {
				return builder.addArgument( ()->Commands.argument( this.name, argument ) );
			}

			@Override
			public Float get( CommandContext< CommandSourceStack > context ) {
				return context.getArgument( this.name, Float.class );
			}
		}.named( DefaultKeys.VALUE );
	}

	public static < EnumType extends Enum< EnumType > > IParameter.Named< EnumType > enumeration( Class< EnumType > clazz ) {
		return new IParameter.Named< EnumType >() {
			@Override
			public CommandBuilder apply( CommandBuilder builder ) {
				return builder.addArgument( ()->Commands.argument( this.name, EnumArgument.enumArgument( clazz ) ) );
			}

			@Override
			public EnumType get( CommandContext< CommandSourceStack > context ) {
				return context.getArgument( this.name, clazz );
			}
		}.named( clazz.getSimpleName().toLowerCase() );
	}

	public static IParameter.Named< Vec3 > position() {
		return new IParameter.Named< Vec3 >() {
			@Override
			public CommandBuilder apply( CommandBuilder builder ) {
				return builder.addArgument( ()->Commands.argument( this.name, Vec3Argument.vec3() ) );
			}

			@Override
			public Vec3 get( CommandContext< CommandSourceStack > context ) {
				return context.getArgument( this.name, Coordinates.class ).getPosition( context.getSource() );
			}
		}.named( DefaultKeys.POSITION );
	}

	public static IParameter.Named< Entity > entity() {
		return new IParameter.Named< Entity >() {
			@Override
			public CommandBuilder apply( CommandBuilder builder ) {
				return builder.addArgument( ()->Commands.argument( this.name, EntityArgument.entity() ) );
			}

			@Override
			public Entity get( CommandContext< CommandSourceStack > context ) {
				try {
					return context.getArgument( this.name, EntitySelector.class ).findSingleEntity( context.getSource() );
				} catch( Throwable exception ) {
					return null;
				}
			}
		}.named( DefaultKeys.ENTITY );
	}

	public static IParameter.Named< List< ? extends Entity > > entities() {
		return new IParameter.Named< List< ? extends Entity > >() {
			@Override
			public CommandBuilder apply( CommandBuilder builder ) {
				return builder.addArgument( ()->Commands.argument( this.name, EntityArgument.entities() ) );
			}

			@Override
			public List< ? extends Entity > get( CommandContext< CommandSourceStack > context ) {
				try {
					return context.getArgument( this.name, EntitySelector.class ).findEntities( context.getSource() );
				} catch( Throwable exception ) {
					return null;
				}
			}
		}.named( DefaultKeys.ENTITIES );
	}

	public static IParameter< List< Vec3 > > anyPosition() {
		return new IParameter<>() {
			@Override
			public CommandBuilder apply( CommandBuilder builder ) {
				List< IModification > modifications = new ArrayList<>();
				modifications.add( ( CommandBuilder subbuilder )->subbuilder.addArgument( Commands.argument( DefaultKeys.POSITION, Vec3Argument.vec3() ) ) );
				modifications.add( ( CommandBuilder subbuilder )->subbuilder.addArgument( Commands.argument( DefaultKeys.ENTITY, EntityArgument.entity() ) ) );
				modifications.add( ( CommandBuilder subbuilder )->subbuilder.addArgument( Commands.argument( DefaultKeys.ENTITIES, EntityArgument.entities() ) ) );

				return builder.add( modifications );
			}

			@Override
			public List< Vec3 > get( CommandContext< CommandSourceStack > context ) {
				List< Vec3 > positions = new ArrayList<>();
				try {
					positions.add( context.getArgument( DefaultKeys.POSITION, Coordinates.class ).getPosition( context.getSource() ) );
				} catch( Throwable ignored ) {}
				try {
					positions.add( context.getArgument( DefaultKeys.ENTITY, EntitySelector.class ).findSingleEntity( context.getSource() ).position() );
				} catch( Throwable ignored ) {}
				try {
					context.getArgument( DefaultKeys.ENTITIES, EntitySelector.class )
						.findEntities( context.getSource() )
						.forEach( entity->positions.add( entity.position() ) );
				} catch( Throwable ignored ) {}

				return positions;
			}
		};
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
		public static final String ENTITY = "entity";
		public static final String VALUE = "value";
		public static final String POSITION = "position";
	}
}
