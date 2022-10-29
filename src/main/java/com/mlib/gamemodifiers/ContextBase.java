package com.mlib.gamemodifiers;

import com.mlib.config.ConfigGroup;
import com.mlib.gamemodifiers.parameters.ContextParameters;
import com.mlib.gamemodifiers.parameters.Parameters;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class ContextBase< DataType extends ContextData > extends ConfigGroup implements IParameterizable {
	final Class< DataType > dataClass;
	final Consumer< DataType > consumer;
	final List< Condition > conditions = new ArrayList<>();
	final ContextParameters params;
	protected GameModifier gameModifier = null;

	@Deprecated
	public static < DataType extends ContextData, ContextType extends ContextBase< DataType > > void accept( List< ContextType > contexts, DataType data ) {
		contexts.forEach( context->{
			if( context.check( data ) ) {
				context.consumer.accept( data );
			}
		} );
	}

	@Deprecated
	public static < ContextType extends ContextBase< ? > > void addSorted( List< ContextType > contexts, ContextType context ) {
		contexts.add( context );
		contexts.sort( ContextParameters.COMPARATOR );
	}

	public ContextBase( Class< DataType > dataClass, Consumer< DataType > consumer, ContextParameters params ) {
		super( params.getConfigName(), params.getConfigComment() );
		this.dataClass = dataClass;
		this.consumer = consumer;
		this.params = params;
	}

	public ContextBase( Class< DataType > dataClass, Consumer< DataType > consumer ) {
		this( dataClass, consumer, new ContextParameters() );
	}

	@Override
	public Parameters getParams() {
		return this.params;
	}

	public void setup( GameModifier gameModifier ) {
		assert this.gameModifier == null : "Context was already set up";
		this.gameModifier = gameModifier;
		this.conditions.forEach( this::addConfig );
	}

	public ContextBase< DataType > addCondition( Condition condition ) {
		assert this.gameModifier == null : "Context was already set up";
		this.conditions.add( condition );
		this.conditions.sort( Parameters.COMPARATOR );

		return this;
	}

	public ContextBase< DataType > addCondition( Predicate< DataType > predicate ) {
		return addCondition( new Condition.Context<>( this.dataClass, predicate ) );
	}

	public ContextBase< DataType > addConditions( Condition... conditions ) {
		for( Condition condition : conditions ) {
			this.addCondition( condition );
		}

		return this;
	}

	@SafeVarargs
	public final ContextBase< DataType > addConditions( Predicate< DataType >... predicates ) {
		for( Predicate< DataType > predicate : predicates ) {
			this.addCondition( predicate );
		}

		return this;
	}

	public boolean check( ContextData data ) {
		for( Condition condition : this.conditions ) {
			if( !condition.check( this.gameModifier, data ) ) {
				return false;
			}
		}

		return true;
	}

	public GameModifier getGameModifier() {
		return this.gameModifier;
	}
}
