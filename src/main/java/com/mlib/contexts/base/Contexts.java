package com.mlib.contexts.base;

import com.mlib.contexts.data.IProfilerData;
import net.minecraft.util.profiling.InactiveProfiler;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class Contexts< DataType > {
	final static Map< Class< ? >, Contexts< ? > > CONTEXTS = Collections.synchronizedMap( new HashMap<>() );
	final List< Context< DataType > > contexts = new ArrayList<>();
	boolean isSorted = true;

	private Contexts() {}

	public static < DataType > Contexts< DataType > get( Class< DataType > clazz ) {
		return ( Contexts< DataType > )CONTEXTS.computeIfAbsent( clazz, key->new Contexts< DataType >() );
	}

	public static Stream< Contexts< ? > > get() {
		return CONTEXTS.values().stream();
	}

	public synchronized Context< DataType > add( Consumer< DataType > consumer ) {
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
		this.contexts.forEach( consumer );
	}

	public synchronized void tryToSort() {
		if( !this.isSorted ) {
			this.contexts.sort( ( left, right )->Priority.COMPARATOR.compare( left.priority, right.priority ) );
			this.contexts.forEach( Context::tryToSort );
			this.isSorted = true;
		}
	}
}
