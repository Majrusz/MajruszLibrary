package net.mlib.contexts.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Context< DataType > {
	final Consumer< DataType > consumer;
	final List< Condition< DataType > > conditions = new ArrayList<>();
	final String profilerName;

	public Context( Consumer< DataType > consumer ) {
		this.consumer = consumer;
		this.profilerName = buildProfilerName( consumer );
	}

	public Context< DataType > addCondition( Condition< DataType > condition ) {
		this.conditions.add( condition );

		return this;
	}

	public void accept( DataType data ) {
		if( this.conditions.stream().allMatch( condition->condition.check( data ) ) ) {
			this.consumer.accept( data );
		}
	}

	public List< Condition< DataType > > getConditions() {
		return Collections.unmodifiableList( this.conditions );
	}

	public String getProfilerName() {
		return this.profilerName;
	}

	private static String buildProfilerName( Consumer< ? > consumer ) {
		String name = consumer.getClass().getName();
		Pattern pattern = Pattern.compile( "(.*)\\$\\$Lambda.*" );
		Matcher matcher = pattern.matcher( name );

		return matcher.find() ? matcher.group( 1 ) : name;
	}
}
