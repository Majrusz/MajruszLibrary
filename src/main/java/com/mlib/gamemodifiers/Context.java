package com.mlib.gamemodifiers;

import com.mlib.config.ConfigGroup;
import com.mlib.config.IConfigurable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class Context< DataType > extends ConfigGroup {
	final Consumer< DataType > consumer;
	final List< Condition< DataType > > conditions = new ArrayList<>();
	Priority priority = Priority.NORMAL;
	GameModifier gameModifier = null;

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
		assert this.gameModifier == null : "Context has already been set up";
		this.conditions.add( condition );

		return this;
	}

	public void insertTo( GameModifier modifier ) {
		assert this.gameModifier == null : "Context has already been set up";
		this.gameModifier = modifier;
		this.conditions.sort( ( left, right )->Priority.COMPARATOR.compare( left.getPriority(), right.getPriority() ) );
		this.conditions.stream()
			.filter( Condition::isConfigurable )
			.forEach( this::addConfig );

		modifier.addConfig( this );
	}

	public void accept( DataType data ) {
		if( this.gameModifier != null && this.conditions.stream().allMatch( condition->condition.check( data ) ) ) {
			this.consumer.accept( data );
		}
	}

	public List< Condition< DataType > > getConditions() {
		return Collections.unmodifiableList( this.conditions );
	}

	public GameModifier getGameModifier() {
		return this.gameModifier;
	}

	public Priority getPriority() {
		return this.priority;
	}
}
