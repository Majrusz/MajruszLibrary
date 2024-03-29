package com.majruszlibrary.math;

import com.majruszlibrary.platform.Side;
import net.minecraft.util.RandomSource;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Random {
	static final RandomSource CLIENT = RandomSource.create();
	static final RandomSource SERVER = RandomSource.create();

	/**
	 Returns random source which depends on current thread, to make sure all functions are
	 thread safe and can be accessed on both server and client at the same time.
	 */
	public static RandomSource getThreadSafe() {
		return Side.isLogicalClient() ? CLIENT : SERVER;
	}

	public static float nextFloat() {
		return Random.getThreadSafe().nextFloat();
	}

	public static float nextFloat( float min, float max ) {
		return ( max - min ) * Random.nextFloat() + min;
	}

	public static float nextFloat( Range< Float > range ) {
		return Random.nextFloat( range.from, range.to );
	}

	public static double nextDouble() {
		return Random.getThreadSafe().nextDouble();
	}

	public static double nextDouble( double min, double max ) {
		return ( max - min ) * Random.nextDouble() + min;
	}

	public static double nextDouble( Range< Double > range ) {
		return Random.nextDouble( range.from, range.to );
	}

	public static double nextGaussian() {
		return Random.getThreadSafe().nextGaussian();
	}

	public static int nextInt() {
		return Random.getThreadSafe().nextInt();
	}

	public static int nextInt( int max ) {
		return Random.getThreadSafe().nextInt( max );
	}

	public static int nextInt( int min, int max ) {
		if( min == max ) {
			return min;
		} else if( min > max ) {
			return max + Random.nextInt( min - max );
		}

		return min + Random.nextInt( max - min );
	}

	public static int nextInt( Range< Integer > range ) {
		return Random.nextInt( range.from, range.to );
	}

	public static int nextSign() {
		return Random.nextBoolean() ? 1 : -1;
	}

	public static boolean nextBoolean() {
		return Random.getThreadSafe().nextBoolean();
	}

	public static AnyPos nextVector( float minX, float maxX, float minY, float maxY, float minZ, float maxZ ) {
		return AnyPos.from( Random.nextFloat( minX, maxX ), Random.nextFloat( minY, maxY ), Random.nextFloat( minZ, maxZ ) );
	}

	public static AnyPos nextVector( double minX, double maxX, double minY, double maxY, double minZ, double maxZ ) {
		return AnyPos.from( Random.nextDouble( minX, maxX ), Random.nextDouble( minY, maxY ), Random.nextDouble( minZ, maxZ ) );
	}

	public static AnyPos nextVector( int minX, int maxX, int minY, int maxY, int minZ, int maxZ ) {
		return AnyPos.from( Random.nextInt( minX, maxX ), Random.nextInt( minY, maxY ), Random.nextInt( minZ, maxZ ) );
	}

	public static AnyPos nextUnitVector() {
		AnyPos pos = nextVector( -1.0, 1.0, -1.0, 1.0, -1.0, 1.0 );

		return pos.len().doubleValue() < 1e-5 ? Random.nextUnitVector() : pos.norm();
	}

	public static < Type > Type next( Type... elements ) {
		return elements.length > 0 ? elements[ Random.nextInt( elements.length ) ] : null;
	}

	public static < Type > Type next( List< Type > elements ) {
		return elements.size() > 0 ? elements.get( Random.nextInt( elements.size() ) ) : null;
	}

	public static < Type > List< Type > next( List< Type > elements, int count ) {
		return Random.getRandomIndices( elements.size() ).stream().limit( count ).map( elements::get ).toList();
	}

	public static < Type > Type next( Set< Type > elements ) {
		return ( Type )Random.next( elements.toArray() );
	}

	public static < Type1, Type2 > Map.Entry< Type1, Type2 > next( Map< Type1, Type2 > elements ) {
		return Random.next( elements.entrySet() );
	}

	public static boolean check( float chance ) {
		return Random.nextFloat() < chance;
	}

	public static boolean check( double chance ) {
		return Random.nextDouble() < chance;
	}

	/**
	 Rounds the value randomly depending on the decimal value.
	 For instance if the value is equal to 21.37, there is a 37% (0.37) chance to return 22 and there is a 63% (1.0-0.37) chance to return 21.
	 */
	public static int round( double value ) {
		int roundedValue = ( int )value;

		return roundedValue + ( Random.check( value - roundedValue ) ? 1 : 0 );
	}

	private static List< Integer > getRandomIndices( int size ) {
		List< Integer > indices = IntStream.iterate( 0, i->i + 1 ).limit( size ).boxed().collect( Collectors.toList() );
		Collections.shuffle( indices );

		return indices;
	}
}
