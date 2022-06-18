package com.mlib.time;

import com.mlib.Utility;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

/** Simple class for delaying certain functions. */
public class Delay {
	private int ticksLeft;
	private final ICallable callable;
	private static final List< Delay > DELAYS = new ArrayList<>();

	public Delay( int ticks, ICallable callable ) {
		this.ticksLeft = ticks;
		this.callable = callable;

		DELAYS.add( this );
	}

	public Delay( double seconds, ICallable callable ) {
		this( Utility.secondsToTicks( seconds ), callable );
	}

	/** Returns whether a delay has expired. (should be called) */
	public boolean hasExpired() {
		return this.ticksLeft <= 0;
	}

	@SubscribeEvent
	public static void onServerTick() {
		for( Delay delay : DELAYS ) {
			if( delay.ticksLeft > 0 ) {
				delay.ticksLeft = delay.ticksLeft - 1;
			} else {
				delay.callable.call();
			}
		}

		DELAYS.removeIf( Delay::hasExpired );
	}

	public interface ICallable {
		void call();
	}
}
