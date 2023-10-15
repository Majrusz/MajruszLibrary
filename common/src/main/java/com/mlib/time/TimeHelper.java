package com.mlib.time;

import com.mlib.annotation.AutoInstance;
import com.mlib.contexts.OnClientTicked;
import com.mlib.contexts.OnServerTicked;
import com.mlib.platform.LogicalSafe;
import com.mlib.platform.Side;

public class TimeHelper {
	private static final int TICKS_IN_SECOND = 20;
	private static final int TICKS_IN_MINUTE = TICKS_IN_SECOND * 60;
	private static final LogicalSafe< Long > COUNTER = LogicalSafe.of( -1L, -1L );

	public static int toTicks( double seconds ) {
		return ( int )( seconds * TICKS_IN_SECOND );
	}

	public static double toSeconds( int ticks ) {
		return ( double )( ticks ) / TICKS_IN_SECOND;
	}

	public static boolean haveTicksPassed( int ticks ) {
		return COUNTER.get() % ticks == 0;
	}

	public static boolean haveSecondsPassed( double seconds ) {
		return TimeHelper.haveTicksPassed( TimeHelper.toTicks( seconds ) );
	}

	public static long getTicks() {
		return COUNTER.get();
	}

	public static float getPartialTicks() {
		return Side.get( ()->()->Side.getMinecraft().getFrameTime(), ()->()->0.0f );
	}

	@AutoInstance
	public static class Updater {
		public Updater() {
			OnClientTicked.listen( data->COUNTER.set( x->x + 1 ) );

			OnServerTicked.listen( data->COUNTER.set( x->x + 1 ) );
		}
	}
}
