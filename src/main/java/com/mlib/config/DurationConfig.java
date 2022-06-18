package com.mlib.config;

import com.mlib.Utility;

/** Class representing single double config value with method to time conversion. (stored in seconds) */
public class DurationConfig extends DoubleConfig {
	public DurationConfig( String name, String comment, boolean requiresWorldRestart, double defaultValue, double minimum, double maximum ) {
		super( name, comment, requiresWorldRestart, defaultValue, minimum, maximum );
	}

	/** Converts stored seconds to minecraft ticks. */
	public int getDuration() {
		return getTicks();
	}

	/** Converts stored seconds to minecraft ticks. */
	public int getTicks() {
		return Utility.secondsToTicks( super.get() );
	}
}
