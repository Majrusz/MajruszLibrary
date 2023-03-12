package com.mlib.gamemodifiers;

import java.util.*;
import java.util.function.Consumer;

public class OnContext {
	final static Map< Class< ? >, Contexts< ? > > CONTEXTS = Collections.synchronizedMap( new HashMap<>() );

	protected OnContext() {}

	protected static < DataType extends ContextData > Contexts< DataType > getContexts( Class< DataType > clazz ) {
		return ( Contexts< DataType > )CONTEXTS.getOrDefault( clazz, new Contexts< DataType >() );
	}

	protected static class Contexts< DataType extends ContextData > {
		final List< ContextBase< DataType > > contexts = Collections.synchronizedList( new ArrayList<>() );
		boolean isSorted = false;

		public ContextBase< DataType > add( Consumer< DataType > consumer ) {
			ContextBase< DataType > context = new ContextBase<>( consumer );
			this.contexts.add( context );
			this.isSorted = false;

			return context;
		}

		public DataType dispatch( DataType data ) {
			this.tryToSort();
			this.contexts.forEach( context->{
				if( context.check( data ) ) {
					context.consumer.accept( data );
				}
			} );

			return data;
		}

		private void tryToSort() {
			if( !this.isSorted ) {
				this.contexts.sort( ( left, right )->Priority.COMPARATOR.compare( left.priority, right.priority ) );
				this.isSorted = true;
			}
		}
	}
}
