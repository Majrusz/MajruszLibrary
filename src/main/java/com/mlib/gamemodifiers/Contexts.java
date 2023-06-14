package com.mlib.gamemodifiers;

import com.mlib.gamemodifiers.data.IProfilerData;
import net.minecraft.util.profiling.InactiveProfiler;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.*;
import java.util.function.Consumer;

public class Contexts< DataType > {
	final static Map< Class< ? >, Contexts< ? > > CONTEXTS = Collections.synchronizedMap( new HashMap<>() );
	final SortedSet< Context< DataType > > contexts = new TreeSet<>( ( left, right )->Priority.COMPARATOR.compare( left.priority, right.priority ) );

	private Contexts() {}

	public static < DataType > Contexts< DataType > get( Class< DataType > clazz ) {
		return ( Contexts< DataType > )CONTEXTS.computeIfAbsent( clazz, key->new Contexts< DataType >() );
	}

	public static Collection< Contexts< ? > > get() {
		return CONTEXTS.values();
	}

	public synchronized Context< DataType > add( Consumer< DataType > consumer ) {
		Context< DataType > context = new Context<>( consumer );
		this.contexts.add( context );

		return context;
	}

	public DataType dispatch( DataType data ) {
		ProfilerFiller profiler = data instanceof IProfilerData profilerData ? profilerData.getProfiler() : InactiveProfiler.INSTANCE;
		profiler.push( data.getClass().getName() );
		this.forEach( context->context.accept( data ) );
		profiler.pop();

		return data;
	}

	public void forEach( Consumer< Context< DataType > > consumer ) {
		this.contexts.forEach( consumer );
	}
}
