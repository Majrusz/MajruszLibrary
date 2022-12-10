package com.mlib.gamemodifiers;

import com.mlib.gamemodifiers.parameters.Parameters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Contexts< DataType extends ContextData, ContextType extends ContextBase< DataType > > {
	final List< ContextType > contexts = Collections.synchronizedList( new ArrayList<>() );
	boolean isSorted = false;

	public void add( ContextType context ) {
		this.contexts.add( context );
	}

	public void accept( DataType data ) {
		if( !this.isSorted ) {
			this.contexts.sort( Parameters.COMPARATOR );
			this.isSorted = true;
		}

		this.contexts.forEach( context->{
			if( context.check( data ) ) {
				context.consumer.accept( data );
			}
		} );
	}
}
