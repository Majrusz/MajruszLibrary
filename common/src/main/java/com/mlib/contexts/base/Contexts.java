package com.mlib.contexts.base;

import com.mlib.contexts.data.ICancellableData;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.util.Mth;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Contexts< DataType > {
	final static Object2ObjectMap< Class< ? >, Contexts< ? > > CONTEXTS = new Object2ObjectOpenHashMap<>();
	final List< Context< DataType > > contexts = new ArrayList<>();

	public static < DataType > DataType dispatch( DataType data ) {
		Contexts< ? > contexts = CONTEXTS.get( data.getClass() );
		if( contexts != null ) {
			( ( Contexts< DataType > )contexts ).accept( data );
		}

		return data;
	}

	public static < DataType extends ICancellableData > DataType dispatch( DataType data ) {
		Contexts< ? > contexts = CONTEXTS.get( data.getClass() );
		if( contexts != null ) {
			( ( Contexts< DataType > )contexts ).accept( data, ICancellableData::isExecutionStopped );
		}

		return data;
	}

	public static < DataType > Contexts< DataType > get( Class< DataType > clazz ) {
		synchronized( Contexts.class ) {
			return ( Contexts< DataType > )CONTEXTS.computeIfAbsent( clazz, key->new Contexts< DataType >() );
		}
	}

	public Context< DataType > add( Consumer< DataType > consumer ) {
		Context< DataType > context = new Context<>( this, consumer );
		synchronized( this ) {
			this.contexts.add( context );
		}
		this.sort();

		return context;
	}

	void sort() {
		synchronized( this ) {
			this.contexts.sort( ( left, right )->Mth.sign( left.priority.ordinal() - right.priority.ordinal() ) );
		}
	}

	private Contexts() {}

	private void accept( DataType data ) {
		for( Context< DataType > context : this.contexts ) {
			context.accept( data );
		}
	}

	private void accept( DataType data, Predicate< DataType > isExecutionStopped ) {
		for( Context< DataType > context : this.contexts ) {
			context.accept( data );
			if( isExecutionStopped.test( data ) ) {
				break;
			}
		}
	}
}
