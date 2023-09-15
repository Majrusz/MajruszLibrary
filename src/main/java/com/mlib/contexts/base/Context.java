package com.mlib.contexts.base;

import com.mlib.config.ConfigGroup;
import com.mlib.config.IConfigurable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Context< DataType > extends ConfigGroup {
	final Consumer< DataType > consumer;
	final List< Condition< DataType > > conditions = new ArrayList<>();
	final String profilerName;
	Priority priority = Priority.NORMAL;
	boolean isSorted = true;

	public Context( Consumer< DataType > consumer ) {
		this.consumer = consumer;
		this.profilerName = buildProfilerName( consumer );
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
		this.isSorted = false;

		return this;
	}

	public void accept( DataType data ) {
		this.tryToSort();
		if( this.conditions.stream().allMatch( condition->condition.check( data ) ) ) {
			this.consumer.accept( data );
		}
	}

	public synchronized void tryToSort() {
		if( !this.isSorted ) {
			this.conditions.sort( ( left, right )->Priority.COMPARATOR.compare( left.getPriority(), right.getPriority() ) );
			this.isSorted = true;
		}
	}

	public List< Condition< DataType > > getConditions() {
		this.tryToSort();

		return Collections.unmodifiableList( this.conditions );
	}

	public String getProfilerName() {
		return this.profilerName;
	}

	public Priority getPriority() {
		return this.priority;
	}

	private static String buildProfilerName( Consumer< ? > consumer ) {
		String name = consumer.getClass().getName();
		Pattern pattern = Pattern.compile( "(.*)\\$\\$Lambda.*" );
		Matcher matcher = pattern.matcher( name );

		return matcher.find() ? matcher.group( 1 ) : name;
	}
}
