package com.mlib.gamemodifiers;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class Contexts< DataType > {
	final static Map< Class< ? >, Contexts< ? > > CONTEXTS = Collections.synchronizedMap( new HashMap<>() );
	final List< ContextBase< DataType > > contexts = Collections.synchronizedList( new ArrayList<>() );
	boolean isSorted = false;

	private Contexts() {}

	public static < DataType > Contexts< DataType > get( Class< DataType > clazz ) {
		return ( Contexts< DataType > )CONTEXTS.computeIfAbsent( clazz, key->new Contexts< DataType >() );
	}

	public static Stream< Contexts< ? > > streamAll() {
		return CONTEXTS.values().stream();
	}

	public ContextBase< DataType > add( Consumer< DataType > consumer ) {
		ContextBase< DataType > context = new ContextBase<>( consumer );
		this.contexts.add( context );
		this.isSorted = false;

		return context;
	}

	public DataType dispatch( DataType data ) {
		this.tryToSort();
		this.contexts.forEach( context->context.accept( data ) );

		return data;
	}

	public Stream< ContextBase< DataType > > stream() {
		return this.contexts.stream();
	}

	private void tryToSort() {
		if( !this.isSorted ) {
			this.contexts.sort( ( left, right )->Priority.COMPARATOR.compare( left.priority, right.priority ) );
			this.isSorted = true;
		}
	}
}
