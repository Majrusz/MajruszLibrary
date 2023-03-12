package com.mlib.gamemodifiers;

import com.mlib.config.ConfigGroup;
import com.mlib.config.IConfigurable;
import com.mlib.gamemodifiers.parameters.Parameters;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

// TODO: rename to Context<>
public class ContextBase< DataType extends ContextData > extends ConfigGroup {
	final Consumer< DataType > consumer;
	final List< Condition< DataType > > conditions = new ArrayList<>();
	protected Priority priority = Priority.NORMAL;
	protected GameModifier gameModifier = null;

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

	public void setup( GameModifier gameModifier ) {
		assert this.gameModifier == null : "Context has already been set up";
		this.gameModifier = gameModifier;
		this.conditions.stream()
			.filter( condition->condition.getParams().isConfigurable() )
			.forEach( this::addConfig );
	}

	public ContextBase< DataType > addCondition( Condition< DataType > condition ) {
		assert this.gameModifier == null : "Context has already been set up";
		this.conditions.add( condition );
		this.conditions.sort( Parameters.COMPARATOR );

		return this;
	}

	public ContextBase< DataType > addCondition( Predicate< DataType > predicate ) {
		return this.addCondition( new Condition.Custom<>( predicate ) );
	}

	public ContextBase< DataType > addCondition( Supplier< Boolean > check ) {
		return this.addCondition( new Condition.Custom<>( data->check.get() ) );
	}

	public void insertTo( GameModifier modifier ) {
		modifier.addContext( this );
	}

	public boolean check( DataType data ) {
		return this.conditions.stream().allMatch( condition->condition.isMet( this.gameModifier, data ) );
	}

	public List< Condition< DataType > > getConditions() {
		return this.conditions;
	}

	public GameModifier getGameModifier() {
		return this.gameModifier;
	}
}
