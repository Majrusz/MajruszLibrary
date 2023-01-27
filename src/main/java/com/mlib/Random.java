package com.mlib;

import com.mlib.config.DoubleConfig;
import com.mlib.math.VectorHelper;
import com.mojang.math.Vector3f;
import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Random {
	static final java.util.Random CLIENT = new java.util.Random();
	static final java.util.Random SERVER = new java.util.Random();

	/**
	 Returns random source which depends on current thread, to make sure all functions are
	 thread safe and can be accessed on both server and client at the same time.
	 */
	public static java.util.Random getThreadSafe() {
		return Utility.isServerSide() ? SERVER : CLIENT;
	}

	public static float nextFloat() {
		return getThreadSafe().nextFloat();
	}

	public static float nextFloat( float min, float max ) {
		return ( max - min ) * nextFloat() + min;
	}

	public static double nextDouble() {
		return getThreadSafe().nextDouble();
	}

	public static double nextDouble( double min, double max ) {
		return ( max - min ) * nextDouble() + min;
	}

	public static double nextGaussian() {
		return getThreadSafe().nextGaussian();
	}

	public static int nextInt() {
		return getThreadSafe().nextInt();
	}

	public static int nextInt( int max ) {
		return getThreadSafe().nextInt( max );
	}

	public static int nextInt( int min, int max ) {
		if( min == max ) {
			return min;
		} else if( min > max ) {
			return max + nextInt( min - max );
		}

		return min + nextInt( max - min );
	}

	public static boolean nextBoolean() {
		return getThreadSafe().nextBoolean();
	}

	public static < Type > Type nextRandom( Type[] elements ) {
		return elements.length > 0 ? elements[ nextInt( elements.length ) ] : null;
	}

	public static < Type > Type nextRandom( List< Type > elements ) {
		return elements.size() > 0 ? elements.get( nextInt( elements.size() ) ) : null;
	}

	public static < Type > Type nextRandom( Set< Type > elements ) {
		return ( Type )nextRandom( elements.toArray() );
	}

	public static < Type1, Type2 > Map.Entry< Type1, Type2 > nextRandom( Map< Type1, Type2 > elements ) {
		return nextRandom( elements.entrySet() );
	}

	public static boolean tryChance( double chance ) {
		return nextDouble() <= chance;
	}

	public static boolean tryChance( DoubleConfig config ) {
		return tryChance( config.get() );
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
		return new Vec3i( nextInt( minX, maxX ), nextInt( minY, maxY ), nextInt( minZ, maxZ ) );
	}

	public static Vec3 getRandomNormalizedVector() {
		Vec3 vector = getRandomVector3d( -1.0, 1.0, -1.0, 1.0, -1.0, 1.0 );
		if( VectorHelper.length( vector ) < 1e-5 ) // to avoid dividing by zero (or very small number) and throwing exception
			return getRandomNormalizedVector();

		return VectorHelper.normalize( vector );
	}
}
