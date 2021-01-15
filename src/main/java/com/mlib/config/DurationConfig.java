package com.mlib.config;

import com.mlib.TimeConverter;

/** Class representing single double config value with method to time conversion. (stored in seconds) */
public class DurationConfig extends DoubleConfig {
	public DurationConfig( String name, String comment, boolean requiresWorldRestart, double defaultValue, double minimum, double maximum ) {
		super( name, comment, requiresWorldRestart, defaultValue, minimum, maximum );
	}

	/** Checks if a value from the file is true. */
	public int getDuration() {
		return TimeConverter.secondsToTicks( super.get() );
	}
}
