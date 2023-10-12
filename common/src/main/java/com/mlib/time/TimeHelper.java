package com.mlib.time;

import com.mlib.annotation.AutoInstance;
import com.mlib.contexts.OnClientTicked;
import com.mlib.contexts.OnServerTicked;
import com.mlib.platform.Platform;

public class TimeHelper {
	private static final int TICKS_IN_SECOND = 20;
	private static final int TICKS_IN_MINUTE = TICKS_IN_SECOND * 60;
	private static long CLIENT_COUNTER = -1;
	private static long SERVER_COUNTER = -1;

	public static int toTicks( double seconds ) {
		return ( int )( seconds * TICKS_IN_SECOND );
	}

	public static double toSeconds( int ticks ) {
		return ( double )( ticks ) / TICKS_IN_SECOND;
	}

	public static boolean haveClientTicksPassed( int ticks ) {
		return CLIENT_COUNTER % ticks == 0;
	}

	public static boolean haveClientSecondsPassed( double seconds ) {
		return TimeHelper.haveClientTicksPassed( TimeHelper.toTicks( seconds ) );
	}

	public static long getClientTicks() {
		return CLIENT_COUNTER;
	}

	public static boolean haveServerTicksPassed( int ticks ) {
		return SERVER_COUNTER % ticks == 0;
	}

	public static boolean haveServerSecondsPassed( double seconds ) {
		return TimeHelper.haveServerTicksPassed( TimeHelper.toTicks( seconds ) );
	}

	public static long getServerTicks() {
		return SERVER_COUNTER;
	}

	public static float getPartialTicks() {
		return Platform.get( ()->()->Platform.getMinecraft().getFrameTime(), ()->()->0.0f );
	}

	@AutoInstance
	public static class Updater {
		public Updater() {
			OnClientTicked.listen( data->++CLIENT_COUNTER );

			OnServerTicked.listen( data->++SERVER_COUNTER );
		}
	}
}
