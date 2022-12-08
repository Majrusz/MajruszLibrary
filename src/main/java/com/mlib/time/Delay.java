package com.mlib.time;

import com.mlib.Utility;
import com.mlib.gamemodifiers.contexts.OnServerTick;
import net.minecraftforge.event.TickEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Deprecated
public class Delay {
	static final List< Delay > PENDING_DELAYS = new ArrayList<>();
	static final List< Delay > DELAYS = new ArrayList<>();

	static {
		new OnServerTick.Context( Delay::update )
			.addCondition( data->data.event.phase != TickEvent.Phase.START );
	}

	public static void onNextTick( ICallable callable ) {
		new Delay( 0, callable );
	}

	int ticksLeft;
	final ICallable callable;

	public Delay( int ticks, ICallable callable ) {
		this.ticksLeft = ticks;
		this.callable = callable;

		PENDING_DELAYS.add( this );
	}

	public Delay( double seconds, ICallable callable ) {
		this( Utility.secondsToTicks( seconds ), callable );
	}

	public boolean isAboutToGetCalled() {
		return this.ticksLeft <= 0;
	}

	private static void update( OnServerTick.Data data ) {
		DELAYS.addAll( PENDING_DELAYS );
		PENDING_DELAYS.clear();
		for( Iterator< Delay > iterator = DELAYS.iterator(); iterator.hasNext(); ) {
			Delay delay = iterator.next();
			if( delay.isAboutToGetCalled() ) {
				delay.callable.call();
				iterator.remove();
			} else {
				delay.ticksLeft = delay.ticksLeft - 1;
			}
		}
	}

	public interface ICallable {
		void call();
	}
}
