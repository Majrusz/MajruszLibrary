package com.mlib.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class CommandBuilder {
	final List< List< IModification > > modifications = new ArrayList<>();
	final List< LiteralArgumentBuilder< CommandSourceStack > > arguments = new ArrayList<>();

	public CommandBuilder add( Predicate< CommandSourceStack > predicate ) {
		return this.add( ()->this.getLastArgument().requires( predicate ) );
	}

	public CommandBuilder literal( String... names ) {
		List< IModification > modifications = new ArrayList<>();
		for( String name : names ) {
			modifications.add( ()->{
				LiteralArgumentBuilder< CommandSourceStack > argument = Commands.literal( name );
				this.tryToMergePreviousArguments();
				this.addArgument( argument );
			} );
		}

		return this.add( modifications );
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
			this.tryToMergePreviousArguments();
			dispatcher.register( this.getFirstArgument() );
		}
	}

	private void addArgument( LiteralArgumentBuilder< CommandSourceStack > argument ) {
		this.arguments.add( argument );
	}

	private void clearArguments() {
		this.arguments.clear();
	}

	private boolean emptyArguments() {
		return this.arguments.size() <= 0;
	}

	private LiteralArgumentBuilder< CommandSourceStack > getFirstArgument() {
		if( this.emptyArguments() )
			throw new IllegalArgumentException();

		return this.arguments.get( 0 );
	}

	private LiteralArgumentBuilder< CommandSourceStack > getLastArgument() {
		if( this.emptyArguments() )
			throw new IllegalArgumentException();

		return this.arguments.get( this.arguments.size() - 1 );
	}

	private void tryToMergePreviousArguments() {
		if( this.arguments.size() < 2 )
			return;

		LiteralArgumentBuilder< CommandSourceStack > previousArgument = this.arguments.get( this.arguments.size() - 2 );
		LiteralArgumentBuilder< CommandSourceStack > nextArgument = this.arguments.get( this.arguments.size() - 1 );

		previousArgument.then( nextArgument );
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
}
