package com.mlib.gamemodifiers;

import com.mlib.config.ConfigGroup;
import com.mlib.config.IConfigurable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

// TODO: rename to Context<>
public class ContextBase< DataType > extends ConfigGroup {
	final Consumer< DataType > consumer;
	final List< Condition< DataType > > conditions = new ArrayList<>();
	Priority priority = Priority.NORMAL;
	GameModifier gameModifier = null;

	public ContextBase( Consumer< DataType > consumer ) {
		this.consumer = consumer;
	}

	@Override
	public ContextBase< DataType > addConfig( IConfigurable config ) {
		super.addConfig( config );

		return this;
	}

	@Override
	public ContextBase< DataType > addConfigs( IConfigurable... configs ) {
		super.addConfigs( configs );

		return this;
	}

	@Override
	public ContextBase< DataType > name( String name ) {
		super.name( name );

		return this;
	}

	@Override
	public ContextBase< DataType > comment( String comment ) {
		super.comment( comment );

		return this;
	}

	@Override
	public ContextBase< DataType > requiresWorldRestart( boolean worldRestartRequired ) {
		super.requiresWorldRestart( worldRestartRequired );

		return this;
	}

	public ContextBase< DataType > priority( Priority priority ) {
		this.priority = priority;

		return this;
	}

	public ContextBase< DataType > addCondition( Condition< DataType > condition ) {
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
		if( this.conditions.stream().allMatch( condition->condition.check( data ) ) ) {
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
