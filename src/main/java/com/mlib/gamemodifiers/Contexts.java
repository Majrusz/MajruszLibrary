package com.mlib.gamemodifiers;

import com.mlib.gamemodifiers.parameters.Parameters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Contexts< DataType extends ContextData, ContextType extends ContextBase< DataType > > {
	static List< Contexts< ? extends ContextData, ? extends ContextBase< ? > > > INSTANCES = new ArrayList<>();
	final List< ContextType > contexts = Collections.synchronizedList( new ArrayList<>() );
	boolean isSorted = false;

	public static List< Contexts< ? extends ContextData, ? extends ContextBase< ? > > > getInstances() {
		return INSTANCES;
	}

	public Contexts() {
		INSTANCES.add( this );
	}

	public void add( ContextType context ) {
		this.contexts.add( context );
		this.isSorted = false;
	}

	public void accept( DataType data ) {
		this.tryToSort();
		this.contexts.forEach( context->{
			if( context.check( data ) ) {
				context.consumer.accept( data );
			}
		} );
	}

	public List< ContextType > getContexts() {
		return Collections.unmodifiableList( this.contexts );
	}

	private void tryToSort() {
		if( !this.isSorted ) {
			this.contexts.sort( Parameters.COMPARATOR );
			this.isSorted = true;
		}
	}
}
