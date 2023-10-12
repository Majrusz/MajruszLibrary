package com.mlib.contexts.base;

import com.mlib.profiler.ProfilerHelper;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class Contexts< DataType > {
	final static Map< Class< ? >, Contexts< ? > > CONTEXTS = Collections.synchronizedMap( new HashMap<>() );
	final List< Context< DataType > > contexts = new ArrayList<>();

	public static < DataType > DataType dispatch( DataType data ) {
		Class< DataType > clazz = ( Class< DataType > )data.getClass();
		ProfilerHelper.profile( clazz.getName(), ()->{
			for( Context< DataType > context : Contexts.get( clazz ).contexts ) {
				context.accept( data );
			}
		} );

		return data;
	}

	public static < DataType > Contexts< DataType > get( Class< DataType > clazz ) {
		return ( Contexts< DataType > )CONTEXTS.computeIfAbsent( clazz, key->new Contexts< DataType >() );
	}

	public static Stream< Contexts< ? > > get() {
		return CONTEXTS.values().stream();
	}

	public synchronized Context< DataType > add( Consumer< DataType > consumer ) {
		Context< DataType > context = new Context<>( consumer );
		this.contexts.add( context );

		return context;
	}

	private Contexts() {}
}
