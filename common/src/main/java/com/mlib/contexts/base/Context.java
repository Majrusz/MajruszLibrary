package com.mlib.contexts.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class Context< DataType > {
	final Contexts< DataType > contexts;
	final Consumer< DataType > consumer;
	final List< Condition< DataType > > conditions = new ArrayList<>();
	Priority priority = Priority.NORMAL;

	public Context( Contexts< DataType > contexts, Consumer< DataType > consumer ) {
		this.contexts = contexts;
		this.consumer = consumer;
	}

	public Context< DataType > addCondition( Condition< DataType > condition ) {
		this.conditions.add( condition );

		return this;
	}

	public Context< DataType > priority( Priority priority ) {
		this.priority = priority;
		this.contexts.sort();

		return this;
	}

	public void accept( DataType data ) {
		for( Condition< DataType > condition : this.conditions ) {
			if( !condition.check( data ) ) {
				return;
			}
		}

		this.consumer.accept( data );
	}

	public List< Condition< DataType > > getConditions() {
		return Collections.unmodifiableList( this.conditions );
	}
}
