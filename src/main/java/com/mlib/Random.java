package com.mlib;

import net.minecraft.util.math.vector.Vector3d;

public class Random {
	/**
	 The method responsible for the random event.

	 @param chance Chance from range [0.0;1.0].

	 @return Returns true if drawn number was from range [0.0;chance], and false otherwise.
	 */
	public static boolean tryChance( double chance ) {
		return MajruszLibrary.RANDOM.nextDouble() <= chance;
	}

	/** Returns random vector 3d. */
	public static Vector3d getRandomVector3d( double minX, double maxX, double minY, double maxY, double minZ, double maxZ ) {
		double x = ( maxX - minX ) * MajruszLibrary.RANDOM.nextDouble() + minX;
		double y = ( maxY - minY ) * MajruszLibrary.RANDOM.nextDouble() + minY;
		double z = ( maxZ - minZ ) * MajruszLibrary.RANDOM.nextDouble() + minZ;

		return new Vector3d( x, y, z );
	}

	/** Returns random normalized vector 3d. (with length = 1) */
	public static Vector3d getRandomNormalizedVector3d() {
		Vector3d vector = getRandomVector3d( -1.0, 1.0, -1.0, 1.0, -1.0, 1.0 );

		if( vector.length() < 1e-6 ) // to avoid dividing by zero (or very small number) and throwing exception
			return getRandomNormalizedVector3d();
		else
			return vector.normalize();
	}
}
