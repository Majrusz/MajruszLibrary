package com.mlib.contexts.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

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

	public Context< DataType > addCondition( Predicate< DataType > predicate ) {
		return this.addCondition( Condition.predicate( predicate ) );
	}

	public Context< DataType > addCondition( Supplier< Boolean > supplier ) {
		return this.addCondition( Condition.predicate( data->supplier.get() ) );
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
