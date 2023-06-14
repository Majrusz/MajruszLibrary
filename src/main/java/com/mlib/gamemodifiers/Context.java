package com.mlib.gamemodifiers;

import com.mlib.config.ConfigGroup;
import com.mlib.config.IConfigurable;

import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Consumer;

public class Context< DataType > extends ConfigGroup {
	final Consumer< DataType > consumer;
	final SortedSet< Condition< DataType > > conditions = new TreeSet<>( ( left, right )->Priority.COMPARATOR.compare( left.getPriority(), right.getPriority() ) );
	Priority priority = Priority.NORMAL;

	public Context( Consumer< DataType > consumer ) {
		this.consumer = consumer;
	}

	@Override
	public Context< DataType > addConfig( IConfigurable config ) {
		super.addConfig( config );

		return this;
	}

	@Override
	public Context< DataType > addConfigs( IConfigurable... configs ) {
		super.addConfigs( configs );

		return this;
	}

	@Override
	public Context< DataType > name( String name ) {
		super.name( name );

		return this;
	}

	@Override
	public Context< DataType > comment( String comment ) {
		super.comment( comment );

		return this;
	}

	@Override
	public Context< DataType > requiresWorldRestart( boolean worldRestartRequired ) {
		super.requiresWorldRestart( worldRestartRequired );

		return this;
	}

	public Context< DataType > priority( Priority priority ) {
		this.priority = priority;

		return this;
	}

	public Context< DataType > addCondition( Condition< DataType > condition ) {
		this.conditions.add( condition );
		if( condition.isConfigurable() ) {
			this.addConfig( condition );
		}

		return this;
	}

	public void accept( DataType data ) {
		if( this.conditions.stream().allMatch( condition->condition.check( data ) ) ) {
			this.consumer.accept( data );
		}
	}

	public SortedSet< Condition< DataType > > getConditions() {
		return Collections.unmodifiableSortedSet( this.conditions );
	}

	public Priority getPriority() {
		return this.priority;
	}
}
