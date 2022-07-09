package com.mlib.gamemodifiers;

import com.mlib.config.ConfigGroup;
import com.mlib.gamemodifiers.parameters.Parameters;
import com.mlib.gamemodifiers.parameters.Priority;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class Context< DataType extends ContextData > extends ConfigGroup implements IParameterizable {
	final Class< DataType > dataClass;
	final Consumer< DataType > consumer;
	final List< Condition > conditions = new ArrayList<>();
	final String configName;
	final String configComment;
	final Parameters params;
	protected GameModifier gameModifier = null;

	public static < DataType extends ContextData, ContextType extends Context< DataType > > void accept( List< ContextType > contexts, DataType data ) {
		contexts.forEach( context->{
			if( context.check( data ) ) {
				context.consumer.accept( data );
			}
		} );
	}

	public static < ContextType extends Context< ? > > void addSorted( List< ContextType > contexts, ContextType context ) {
		contexts.add( context );
		contexts.sort( Parameters.COMPARATOR );
	}

	public Context( Class< DataType > dataClass, Consumer< DataType > consumer, String configName, String configComment, Parameters params ) {
		this.dataClass = dataClass;
		this.consumer = consumer;
		this.configName = configName;
		this.configComment = configComment;
		this.params = params;
	}

	public Context( Class< DataType > dataClass, Consumer< DataType > consumer, String configName, String configComment ) {
		this( dataClass, consumer, configName, configComment, new Parameters( Priority.NORMAL ) );
	}

	@Override
	public Parameters getParams() {
		return this.params;
	}

	public void setup( GameModifier gameModifier ) {
		assert this.gameModifier == null : "Context was already set up";
		this.gameModifier = gameModifier;
		this.configs.addAll( this.conditions );
	}

	public Context< DataType > addCondition( Condition condition ) {
		assert this.gameModifier == null : "Context was already set up";
		this.conditions.add( condition );
		this.conditions.sort( Parameters.COMPARATOR );

		return this;
	}

	public Context< DataType > addCondition( Predicate< DataType > predicate ) {
		return addCondition( new Condition.Context<>( this.dataClass, predicate ) );
	}

	public Context< DataType > addConditions( Condition... conditions ) {
		for( Condition condition : conditions ) {
			this.addCondition( condition );
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
