package com.mlib;

import com.mlib.math.VectorHelper;
import com.mojang.math.Vector3f;
import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.Vec3;

public class Random {
	public static float nextFloat() {
		return MajruszLibrary.RANDOM.nextFloat();
	}

	public static float nextFloat( float min, float max ) {
		return ( max - min ) * MajruszLibrary.RANDOM.nextFloat() + min;
	}

	public static double nextDouble() {
		return MajruszLibrary.RANDOM.nextDouble();
	}

	public static double nextDouble( double min, double max ) {
		return ( max - min ) * MajruszLibrary.RANDOM.nextDouble() + min;
	}

	public static int nextInt() {
		return MajruszLibrary.RANDOM.nextInt();
	}

	public static int nextInt( int max ) {
		return MajruszLibrary.RANDOM.nextInt( max );
	}

	public static int nextInt( int min, int max ) {
		return min + nextInt( max - min );
	}

	public static boolean nextBoolean() {
		return MajruszLibrary.RANDOM.nextBoolean();
	}

	public static < Type > Type nextRandom( Type[] elements ) {
		return elements[ nextInt( elements.length ) ];
	}

	/**
	 The method responsible for the random event.

	 @param chance Chance from range [0.0;1.0].

	 @return Returns true if drawn number was from range [0.0;chance], and false otherwise.
	 */
	public static boolean tryChance( double chance ) {
		return nextDouble() <= chance;
	}

	/**
	 Rounds the value randomly depending on the decimal value.
	 For instance if the value is equal to 21.37 then:
	 There is a 37% (0.37) chance to return 22,
	 and there is a 63% (1.0-0.37) chance to return 21.
	 */
	public static int roundRandomly( double experience ) {
		int outputExperience = ( int )experience;

		return outputExperience + ( tryChance( experience - outputExperience ) ? 1 : 0 );
	}

	public static Vector3f getRandomVector3f( float minX, float maxX, float minY, float maxY, float minZ, float maxZ ) {
		return new Vector3f( nextFloat( minX, maxX ), nextFloat( minY, maxY ), nextFloat( minZ, maxZ ) );
	}

	public static Vec3 getRandomVector3d( double minX, double maxX, double minY, double maxY, double minZ, double maxZ ) {
		return new Vec3( nextDouble( minX, maxX ), nextDouble( minY, maxY ), nextDouble( minZ, maxZ ) );
	}

	public static Vec3i getRandomVector3i( int minX, int maxX, int minY, int maxY, int minZ, int maxZ ) {
		return new Vec3i( nextInt( minX, maxX + 1 ), nextInt( minY, maxY + 1 ), nextInt( minZ, maxZ + 1 ) );
	}

	public static Vec3 getRandomNormalizedVector() {
		Vec3 vector = getRandomVector3d( -1.0, 1.0, -1.0, 1.0, -1.0, 1.0 );
		if( VectorHelper.length( vector ) < 1e-5 ) // to avoid dividing by zero (or very small number) and throwing exception
			return getRandomNormalizedVector();

		return VectorHelper.normalize( vector );
	}
}
