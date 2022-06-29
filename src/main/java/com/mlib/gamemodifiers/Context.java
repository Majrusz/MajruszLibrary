package com.mlib.gamemodifiers;

import com.mlib.config.ConfigGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class Context< DataType extends ContextData > extends ConfigGroup {
	final Consumer< DataType > consumer;
	final List< Condition > conditions = new ArrayList<>();
	final String configName;
	final String configComment;
	protected GameModifier gameModifier = null;

	public static < DataType extends ContextData, ContextType extends Context< DataType > > void handleContexts( DataType data,
		List< ContextType > contexts
	) {
		contexts.forEach( context->{
			if( context.check( data ) ) {
				context.consumer.accept( data );
			}
		} );
	}

	public Context( Consumer< DataType > consumer, String configName, String configComment ) {
		this.consumer = consumer;
		this.configName = configName;
		this.configComment = configComment;
	}

	public void setup( GameModifier gameModifier ) {
		assert this.gameModifier == null : "Context was already set up";
		this.gameModifier = gameModifier;
		this.configs.addAll( this.conditions );
	}

	public Context< DataType > addCondition( Condition condition ) {
		assert this.gameModifier == null : "Context was already set up";
		this.conditions.add( condition );
		this.conditions.sort( Condition.COMPARATOR );

		return this;
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
