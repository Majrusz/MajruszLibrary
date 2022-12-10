package com.mlib.gamemodifiers;

import com.mlib.gamemodifiers.parameters.Parameters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class Contexts< DataType extends ContextData, ContextType extends ContextBase< DataType > > {
	static List< Contexts< ?, ? > > INSTANCES = new ArrayList<>();
	final List< ContextType > contexts = Collections.synchronizedList( new ArrayList<>() );
	boolean isSorted = false;

	public static List< Contexts< ?, ? > > getInstances() {
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
		this.forEach( context->{
			if( context.check( data ) ) {
				context.consumer.accept( data );
			}
		} );
	}

	public void forEach( Consumer< ContextType > consumer ) {
		this.tryToSort();
		this.contexts.forEach( consumer );
	}

	private void tryToSort() {
		if( !this.isSorted ) {
			this.contexts.sort( Parameters.COMPARATOR );
			this.isSorted = true;
		}
	}
}
