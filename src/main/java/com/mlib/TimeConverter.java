package com.mlib;

/** Converter for more explicit time conversion in minecraft. */
public class TimeConverter {
	private static final int TICKS_IN_SECOND = 20;
	private static final int TICKS_IN_MINUTE = TICKS_IN_SECOND * 60;

	/** Converts seconds to ticks. */
	public static int secondsToTicks( double seconds ) {
		return ( int )( seconds * TICKS_IN_SECOND );
	}

	/** Converts ticks to seconds. */
	public static double ticksToSeconds( int ticks ) {
		return ( double )( ticks ) / TICKS_IN_SECOND;
	}

	/** Converts minutes to ticks. */
	public static int minutesToTicks( double minutes ) {
		return ( int )( minutes * TICKS_IN_MINUTE );
	}

	/** Converts ticks to minutes. */
	public static double ticksToMinutes( int ticks ) {
		return ( double )( ticks ) / TICKS_IN_MINUTE;
	}
}
