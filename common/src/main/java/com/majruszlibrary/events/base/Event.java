package com.majruszlibrary.events.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Event< DataType > {
	final Events< DataType > events;
	final Consumer< DataType > consumer;
	final List< Condition< DataType > > conditions = new ArrayList<>();
	Priority priority = Priority.NORMAL;

	public Event( Events< DataType > events, Consumer< DataType > consumer ) {
		this.events = events;
		this.consumer = consumer;
	}

	public Event< DataType > addCondition( Condition< DataType > condition ) {
		this.conditions.add( condition );

		return this;
	}

	public Event< DataType > addCondition( Predicate< DataType > predicate ) {
		return this.addCondition( Condition.predicate( predicate ) );
	}

	public Event< DataType > addCondition( Supplier< Boolean > supplier ) {
		return this.addCondition( Condition.predicate( data->supplier.get() ) );
	}

	public Event< DataType > priority( Priority priority ) {
		this.priority = priority;
		this.events.sort();

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
