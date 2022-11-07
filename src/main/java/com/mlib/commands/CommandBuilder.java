package com.mlib.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.coordinates.Coordinates;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.server.command.EnumArgument;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class CommandBuilder {
	final List< List< IModification > > modifications = new ArrayList<>();
	final List< ArgumentBuilder< CommandSourceStack, ? > > arguments = new ArrayList<>();

	public CommandBuilder add( Predicate< CommandSourceStack > predicate ) {
		return this.add( ()->this.getLastArgument().requires( predicate ) );
	}

	public CommandBuilder addArgument( Supplier< ArgumentBuilder< CommandSourceStack, ? > > argument ) {
		return this.add( ()->this.addArgument( argument.get() ) );
	}

	public CommandBuilder literal( String... names ) {
		List< IModification > modifications = new ArrayList<>();
		for( String name : names ) {
			modifications.add( ()->this.addArgument( Commands.literal( name ) ) );
		}

		return this.add( modifications );
	}

	public CommandBuilder integer() {
		return this.integer( DefaultKeys.INT );
	}

	public CommandBuilder integer( String name ) {
		return this.addArgument( ()->Commands.argument( name, IntegerArgumentType.integer() ) );
	}

	public CommandBuilder integer( int min ) {
		return this.integer( DefaultKeys.INT, min );
	}

	public CommandBuilder integer( String name, int min ) {
		return this.addArgument( ()->Commands.argument( name, IntegerArgumentType.integer( min ) ) );
	}

	public CommandBuilder integer( int min, int max ) {
		return this.integer( DefaultKeys.INT, min, max );
	}

	public CommandBuilder integer( String name, int min, int max ) {
		return this.addArgument( ()->Commands.argument( name, IntegerArgumentType.integer( min, max ) ) );
	}

	public < EnumType extends Enum< EnumType > > CommandBuilder enumeration( Class< EnumType > enumClass ) {
		return this.enumeration( DefaultKeys.ENUM, enumClass );
	}

	public < EnumType extends Enum< EnumType > > CommandBuilder enumeration( String name, Class< EnumType > enumClass ) {
		return this.addArgument( ()->Commands.argument( name, EnumArgument.enumArgument( enumClass ) ) );
	}

	public CommandBuilder position() {
		return this.position( DefaultKeys.POSITION );
	}

	public CommandBuilder position( String name ) {
		return this.addArgument( ()->Commands.argument( name, Vec3Argument.vec3() ) );
	}

	public CommandBuilder entity() {
		return this.entity( DefaultKeys.ENTITY );
	}

	public CommandBuilder entity( String name ) {
		return this.addArgument( ()->Commands.argument( name, EntityArgument.entity() ) );
	}

	public CommandBuilder hasPermission( int requiredLevel ) {
		return this.add( ( CommandSourceStack stack )->stack.hasPermission( requiredLevel ) );
	}

	public CommandBuilder execute( IExecutable executable ) {
		return this.add( ()->this.getLastArgument().executes( context->executable.execute( context ) ) );
	}

	public void register( CommandDispatcher< CommandSourceStack > dispatcher ) {
		List< List< Integer > > permutations = this.generatePermutations();
		for( List< Integer > permutation : permutations ) {
			this.clearArguments();
			for( int idx = 0; idx < permutation.size(); ++idx ) {
				this.modifications.get( idx ).get( permutation.get( idx ) ).apply();
			}
			this.mergeArguments();
			try {
				dispatcher.register( ( LiteralArgumentBuilder< CommandSourceStack > )this.getFirstArgument() );
			} catch( Exception exception ) {
				throw new IllegalArgumentException( "First argument of any command must be a literal" );
			}
		}
	}

	private void addArgument( ArgumentBuilder< CommandSourceStack, ? > argument ) {
		this.arguments.add( argument );
	}

	private void clearArguments() {
		this.arguments.clear();
	}

	private boolean emptyArguments() {
		return this.arguments.size() <= 0;
	}

	private ArgumentBuilder< CommandSourceStack, ? > getFirstArgument() {
		if( this.emptyArguments() )
			throw new IllegalArgumentException();

		return this.arguments.get( 0 );
	}

	private ArgumentBuilder< CommandSourceStack, ? > getLastArgument() {
		if( this.emptyArguments() )
			throw new IllegalArgumentException();

		return this.arguments.get( this.arguments.size() - 1 );
	}

	private void mergeArguments() {
		if( this.arguments.size() < 2 )
			return;

		for( int idx = this.arguments.size(); idx >= 2; --idx ) {
			ArgumentBuilder< CommandSourceStack, ? > previousArgument = this.arguments.get( idx - 2 );
			ArgumentBuilder< CommandSourceStack, ? > nextArgument = this.arguments.get( idx - 1 );

			previousArgument.then( nextArgument );
		}
	}

	private List< List< Integer > > generatePermutations() {
		List< List< Integer > > permutations = new ArrayList<>();
		permutations.add( new ArrayList<>() );
		for( List< IModification > modification : this.modifications ) {
			List< List< Integer > > newPermutations = new ArrayList<>();
			for( int idx = 0; idx < modification.size(); ++idx ) {
				List< List< Integer > > copy = new ArrayList<>();
				for( List< Integer > permutation : permutations ) {
					copy.add( new ArrayList<>( permutation ) );
				}
				for( List< Integer > permutation : copy ) {
					permutation.add( idx );
				}
				newPermutations.addAll( copy );
			}
			permutations = newPermutations;
		}
		return permutations;
	}

	private CommandBuilder add( IModification modification ) {
		List< IModification > modifications = new ArrayList<>();
		modifications.add( modification );

		return this.add( modifications );
	}

	private CommandBuilder add( List< IModification > modifications ) {
		this.modifications.add( modifications );

		return this;
	}

	@FunctionalInterface
	interface IModification {
		void apply();
	}

	@FunctionalInterface
	interface IExecutable {
		int execute( CommandContext< CommandSourceStack > context );
	}

	public static class DefaultKeys {
		public static final String ENTITY = "entity";
		public static final String ENUM = "enum";
		public static final String INT = "value";
		public static final String POSITION = "position";
	}
}
