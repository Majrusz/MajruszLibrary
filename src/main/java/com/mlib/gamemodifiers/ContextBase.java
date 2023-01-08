package com.mlib.gamemodifiers;

import com.mlib.config.ConfigGroup;
import com.mlib.config.IConfigurable;
import com.mlib.gamemodifiers.parameters.Parameters;
import com.mlib.gamemodifiers.parameters.Priority;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public abstract class ContextBase< DataType extends ContextData > extends ConfigGroup implements IParameterizable< Parameters > {
	final Consumer< DataType > consumer;
	final List< Condition< DataType > > conditions = new ArrayList<>();
	final Parameters params = new Parameters();
	protected GameModifier gameModifier = null;

	public ContextBase( Consumer< DataType > consumer ) {
		this.consumer = consumer;
	}

	@Override
	public Parameters getParams() {
		return this.params;
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

	public ContextBase< DataType > name( String name ) {
		super.name( name );

		return this;
	}

	public ContextBase< DataType > comment( String comment ) {
		super.comment( comment );

		return this;
	}

	public ContextBase< DataType > requiresWorldRestart( boolean worldRestartRequired ) {
		super.requiresWorldRestart( worldRestartRequired );

		return this;
	}

	public ContextBase< DataType > priority( Priority priority ) {
		this.params.priority( priority );

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

	@SafeVarargs
	public final ContextBase< DataType > addConditions( Condition< DataType >... conditions ) {
		Stream.of( conditions ).forEach( this::addCondition );

		return this;
	}

	@SafeVarargs
	public final ContextBase< DataType > addConditions( Predicate< DataType >... predicates ) {
		Stream.of( predicates ).forEach( this::addCondition );

		return this;
	}

	@SafeVarargs
	public final ContextBase< DataType > addConditions( Supplier< Boolean >... checks ) {
		Stream.of( checks ).forEach( this::addCondition );

		return this;
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
