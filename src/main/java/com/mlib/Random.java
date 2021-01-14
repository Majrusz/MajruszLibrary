package com.mlib;

public class Random {
	/**
	 The method responsible for the random event.

	 @param chance Chance from range [0.0;1.0].

	 @return Returns true if drawn number was from range [0.0;chance], and false otherwise.
	 */
	public static boolean tryChance( double chance ) {
		return MajruszLibrary.RANDOM.nextDouble() <= chance;
	}
}
