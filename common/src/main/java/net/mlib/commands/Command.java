package net.mlib.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.mlib.contexts.OnCommandsInitialized;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Command {
	public static Builder create() {
		return new Builder();
	}

	private Command() {}

	public static class Builder {
		final List< List< IModification > > modifications;
		final List< ArgumentBuilder< CommandSourceStack, ? > > arguments;

		public Builder copy() {
			Builder copy = new Builder();
			this.modifications.forEach( modification->copy.modifications.add( new ArrayList<>( modification ) ) );
			copy.arguments.addAll( this.arguments );

			return copy;
		}

		public Builder add( Predicate< CommandSourceStack > predicate ) {
			return this.add( ( Builder builder )->builder.getLastArgument().requires( predicate ) );
		}

		public Builder addArgument( Supplier< ArgumentBuilder< CommandSourceStack, ? > > argument ) {
			return this.add( ( Builder builder )->builder.addArgument( argument.get() ) );
		}

		public Builder literal( String... names ) {
			List< IModification > modifications = new ArrayList<>();
			for( String name : names ) {
				modifications.add( ( Builder builder )->builder.addArgument( Commands.literal( name ) ) );
			}

			return this.add( modifications );
		}

		public Builder integer() {
			return this.integer( DefaultKeys.INT );
		}

		public Builder integer( String name ) {
			return this.addArgument( ()->Commands.argument( name, IntegerArgumentType.integer() ) );
		}

		public Builder integer( int min ) {
			return this.integer( DefaultKeys.INT, min );
		}

		public Builder integer( String name, int min ) {
			return this.addArgument( ()->Commands.argument( name, IntegerArgumentType.integer( min ) ) );
		}

		public Builder integer( int min, int max ) {
			return this.integer( DefaultKeys.INT, min, max );
		}

		public Builder integer( String name, int min, int max ) {
			return this.addArgument( ()->Commands.argument( name, IntegerArgumentType.integer( min, max ) ) );
		}

		public Builder position() {
			return this.position( DefaultKeys.POSITION );
		}

		public Builder position( String name ) {
			return this.addArgument( ()->Commands.argument( name, Vec3Argument.vec3() ) );
		}

		public Builder entity() {
			return this.entity( DefaultKeys.ENTITY );
		}

		public Builder entity( String name ) {
			return this.addArgument( ()->Commands.argument( name, EntityArgument.entity() ) );
		}

		public Builder entities() {
			return this.entities( DefaultKeys.ENTITIES );
		}

		public Builder entities( String name ) {
			return this.addArgument( ()->Commands.argument( name, EntityArgument.entities() ) );
		}

		public Builder anyPosition() {
			List< IModification > modifications = new ArrayList<>();
			modifications.add( ( Builder builder )->builder.addArgument( Commands.argument( DefaultKeys.POSITION, Vec3Argument.vec3() ) ) );
			modifications.add( ( Builder builder )->builder.addArgument( Commands.argument( DefaultKeys.ENTITY, EntityArgument.entity() ) ) );
			modifications.add( ( Builder builder )->builder.addArgument( Commands.argument( DefaultKeys.ENTITIES, EntityArgument.entities() ) ) );

			return this.add( modifications );
		}

		public Builder hasPermission( int requiredLevel ) {
			return this.add( ( CommandSourceStack stack )->stack.hasPermission( requiredLevel ) );
		}

		public Builder isPlayer() {
			return this.add( CommandSourceStack::isPlayer );
		}

		public Builder execute( IExecutable executable ) {
			return this.add( ( Builder builder )->builder.getLastArgument().executes( context->executable.execute( new CommandData( context ) ) ) );
		}

		public void register() {
			OnCommandsInitialized.listen( this::register );
		}

		private Builder() {
			this.modifications = new ArrayList<>();
			this.arguments = new ArrayList<>();
		}

		private void register( OnCommandsInitialized.Data data ) {
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

		private Builder add( IModification modification ) {
			List< IModification > modifications = new ArrayList<>();
			modifications.add( modification );

			return this.add( modifications );
		}

		private Builder add( List< IModification > modifications ) {
			this.modifications.add( modifications );

			return this;
		}
	}

	@FunctionalInterface
	public interface IModification {
		void apply( Builder builder );
	}

	@FunctionalInterface
	public interface IExecutable {
		int execute( CommandData data ) throws CommandSyntaxException;
	}

	public static class DefaultKeys {
		public static final String ENTITIES = "entities";
		public static final String ENTITY = "entity";
		public static final String INT = "value";
		public static final String POSITION = "position";
	}
}
