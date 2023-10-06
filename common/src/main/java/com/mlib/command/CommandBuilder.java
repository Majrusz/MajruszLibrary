package com.mlib.command;

import com.mlib.contexts.OnCommandsInitialized;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class CommandBuilder {
	final List< List< Command.IModification > > modifications;
	final List< ArgumentBuilder< CommandSourceStack, ? > > arguments;

	public CommandBuilder copy() {
		CommandBuilder copy = new CommandBuilder();
		this.modifications.forEach( modification->copy.modifications.add( new ArrayList<>( modification ) ) );
		copy.arguments.addAll( this.arguments );

		return copy;
	}

	public CommandBuilder add( Predicate< CommandSourceStack > predicate ) {
		return this.add( ( CommandBuilder builder )->builder.getLastArgument().requires( predicate ) );
	}

	public CommandBuilder addArgument( Supplier< ArgumentBuilder< CommandSourceStack, ? > > argument ) {
		return this.add( ( CommandBuilder builder )->builder.addArgument( argument.get() ) );
	}

	public CommandBuilder literal( String... names ) {
		List< Command.IModification > modifications = new ArrayList<>();
		for( String name : names ) {
			modifications.add( ( CommandBuilder builder )->builder.addArgument( Commands.literal( name ) ) );
		}

		return this.add( modifications );
	}

	public CommandBuilder parameter( IParameter< ? > parameter ) {
		return parameter.apply( this );
	}

	public CommandBuilder hasPermission( int requiredLevel ) {
		return this.add( ( CommandSourceStack stack )->stack.hasPermission( requiredLevel ) );
	}

	public CommandBuilder isPlayer() {
		return this.add( CommandSourceStack::isPlayer );
	}

	public CommandBuilder execute( Command.IExecutable executable ) {
		return this.add( ( CommandBuilder builder )->builder.getLastArgument().executes( context->executable.execute( new CommandData( context ) ) ) );
	}

	public void register() {
		OnCommandsInitialized.listen( this::register );
	}

	CommandBuilder() {
		this.modifications = new ArrayList<>();
		this.arguments = new ArrayList<>();
	}

	void register( OnCommandsInitialized data ) {
		List< List< Integer > > permutations = this.generatePermutations();
		for( List< Integer > permutation : permutations ) {
			this.clearArguments();
			for( int idx = 0; idx < permutation.size(); ++idx ) {
				this.modifications.get( idx ).get( permutation.get( idx ) ).apply( this );
			}
			this.mergeArguments();
			try {
				data.dispatcher.register( ( LiteralArgumentBuilder< CommandSourceStack > )this.getFirstArgument() );
			} catch( Exception exception ) {
				throw new IllegalArgumentException( "First argument of any command must be a literal" );
			}
		}
	}

	void addArgument( ArgumentBuilder< CommandSourceStack, ? > argument ) {
		this.arguments.add( argument );
	}

	void clearArguments() {
		this.arguments.clear();
	}

	boolean emptyArguments() {
		return this.arguments.size() <= 0;
	}

	ArgumentBuilder< CommandSourceStack, ? > getFirstArgument() {
		if( this.emptyArguments() ) {
			throw new IllegalArgumentException();
		}

		return this.arguments.get( 0 );
	}

	ArgumentBuilder< CommandSourceStack, ? > getLastArgument() {
		if( this.emptyArguments() ) {
			throw new IllegalArgumentException();
		}

		return this.arguments.get( this.arguments.size() - 1 );
	}

	void mergeArguments() {
		for( int idx = this.arguments.size(); idx >= 2; --idx ) {
			ArgumentBuilder< CommandSourceStack, ? > previousArgument = this.arguments.get( idx - 2 );
			ArgumentBuilder< CommandSourceStack, ? > nextArgument = this.arguments.get( idx - 1 );

			previousArgument.then( nextArgument );
		}
	}

	List< List< Integer > > generatePermutations() {
		List< List< Integer > > permutations = new ArrayList<>();
		permutations.add( new ArrayList<>() );
		for( List< Command.IModification > modification : this.modifications ) {
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

	CommandBuilder add( Command.IModification modification ) {
		List< Command.IModification > modifications = new ArrayList<>();
		modifications.add( modification );

		return this.add( modifications );
	}

	CommandBuilder add( List< Command.IModification > modifications ) {
		this.modifications.add( modifications );

		return this;
	}
}

