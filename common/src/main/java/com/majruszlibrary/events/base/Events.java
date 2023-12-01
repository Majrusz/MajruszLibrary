package com.majruszlibrary.events.base;

import com.majruszlibrary.events.type.ICancellableEvent;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.util.Mth;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Events< DataType > {
	final static Object2ObjectMap< Class< ? >, Events< ? > > EVENTS = new Object2ObjectOpenHashMap<>();
	final List< Event< DataType > > events = new ArrayList<>();

	public static < DataType > DataType dispatch( DataType data ) {
		Events< ? > events = EVENTS.get( data.getClass() );
		if( events != null ) {
			( ( Events< DataType > )events ).accept( data );
		}

		return data;
	}

	public static < DataType extends ICancellableEvent > DataType dispatch( DataType data ) {
		Events< ? > events = EVENTS.get( data.getClass() );
		if( events != null ) {
			( ( Events< DataType > )events ).accept( data, ICancellableEvent::isExecutionStopped );
		}

		return data;
	}

	public static < DataType > Events< DataType > get( Class< DataType > clazz ) {
		synchronized( Events.class ) {
			return ( Events< DataType > )EVENTS.computeIfAbsent( clazz, key->new Events< DataType >() );
		}
	}

	public Event< DataType > add( Consumer< DataType > consumer ) {
		Event< DataType > event = new Event<>( this, consumer );
		synchronized( this ) {
			this.events.add( event );
		}
		this.sort();

		return event;
	}

	void sort() {
		synchronized( this ) {
			this.events.sort( ( left, right )->Mth.sign( left.priority.ordinal() - right.priority.ordinal() ) );
		}
	}

	private Events() {}

	private void accept( DataType data ) {
		for( Event< DataType > event : this.events ) {
			event.accept( data );
		}
	}

	private void accept( DataType data, Predicate< DataType > isExecutionStopped ) {
		for( Event< DataType > event : this.events ) {
			event.accept( data );
			if( isExecutionStopped.test( data ) ) {
				break;
			}
		}
	}
}
