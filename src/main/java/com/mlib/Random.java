package com.mlib;

import com.mlib.math.VectorHelper;
import com.mojang.math.Vector3f;
import net.minecraft.world.phys.Vec3;

public class Random {
	/**
	 The method responsible for the random event.

	 @param chance Chance from range [0.0;1.0].

	 @return Returns true if drawn number was from range [0.0;chance], and false otherwise.
	 */
	public static boolean tryChance( double chance ) {
		return MajruszLibrary.RANDOM.nextDouble() <= chance;
	}

	/**
	 Randomizes experience.
	 For example if experience to randomize is equal 21.37 then:
	 There is 37% (0.37) for 22 experience.
	 And there is 63% (1.0-0.37) for 21 experience.
	 */
	public static int randomizeExperience( double experience ) {
		int outputExperience = ( int )experience;
		if( tryChance( experience - outputExperience ) )
			outputExperience++;

		return outputExperience;
	}

	/** Returns random float vector. */
	public static Vector3f getRandomVector3f( float minX, float maxX, float minY, float maxY, float minZ, float maxZ ) {
		float x = ( maxX - minX ) * MajruszLibrary.RANDOM.nextFloat() + minX;
		float y = ( maxY - minY ) * MajruszLibrary.RANDOM.nextFloat() + minY;
		float z = ( maxZ - minZ ) * MajruszLibrary.RANDOM.nextFloat() + minZ;

		return new Vector3f( x, y, z );
	}

	/** Returns random double vector. */
	public static Vec3 getRandomVector3d( double minX, double maxX, double minY, double maxY, double minZ, double maxZ ) {
		double x = ( maxX - minX ) * MajruszLibrary.RANDOM.nextDouble() + minX;
		double y = ( maxY - minY ) * MajruszLibrary.RANDOM.nextDouble() + minY;
		double z = ( maxZ - minZ ) * MajruszLibrary.RANDOM.nextDouble() + minZ;

		return new Vec3( x, y, z );
	}

	/** Returns random normalized vector. (with distance equal 1) */
	public static Vec3 getRandomNormalizedVector() {
		Vec3 vector = getRandomVector3d( -1.0, 1.0, -1.0, 1.0, -1.0, 1.0 );

		if( VectorHelper.length( vector ) < 1e-5 ) // to avoid dividing by zero (or very small number) and throwing exception
			return getRandomNormalizedVector();
		else
			return VectorHelper.normalize( vector );
	}
}
