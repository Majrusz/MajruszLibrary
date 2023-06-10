package com.mlib.gamemodifiers;

import com.mlib.gamemodifiers.data.IProfilerData;
import net.minecraft.util.profiling.InactiveProfiler;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class Contexts< DataType > {
	final static Map< Class< ? >, Contexts< ? > > CONTEXTS = Collections.synchronizedMap( new HashMap<>() );
	final List< Context< DataType > > contexts = Collections.synchronizedList( new ArrayList<>() );
	boolean isSorted = false;

	private Contexts() {}

	public static < DataType > Contexts< DataType > get( Class< DataType > clazz ) {
		return ( Contexts< DataType > )CONTEXTS.computeIfAbsent( clazz, key->new Contexts< DataType >() );
	}

	public static Stream< Contexts< ? > > streamAll() {
		return CONTEXTS.values().stream();
	}

	public Context< DataType > add( Consumer< DataType > consumer ) {
		Context< DataType > context = new Context<>( consumer );
		this.contexts.add( context );
		this.isSorted = false;

		return context;
	}

	public DataType dispatch( DataType data ) {
		ProfilerFiller profiler = data instanceof IProfilerData profilerData ? profilerData.getProfiler() : InactiveProfiler.INSTANCE;
		profiler.push( data.getClass().getName() );
		this.tryToSort();
		this.forEach( context->context.accept( data ) );
		profiler.pop();

		return data;
	}

	public void forEach( Consumer< Context< DataType > > consumer ) {
		// NOTE: it uses index loop on purpose to avoid deadlocks and concurrent modification exceptions on recursive calls
		for( int idx = 0; idx < this.contexts.size(); ++idx ) {
			consumer.accept( this.contexts.get( idx ) );
		}
	}

	private void tryToSort() {
		if( !this.isSorted ) {
			this.contexts.sort( ( left, right )->Priority.COMPARATOR.compare( left.priority, right.priority ) );
			this.isSorted = true;
		}
	}
}
