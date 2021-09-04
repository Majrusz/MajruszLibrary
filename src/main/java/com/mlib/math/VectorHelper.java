package com.mlib.math;

import com.mojang.math.Vector3d;
import com.mojang.math.Vector3f;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

/**
 Class with few functions for common vector operations.
 Added because there were not enough functions in Minecraft 1.17.1.
 */
public class VectorHelper {
	/** Returns sum of two float vectors. */
	public static Vector3f add( Vector3f vector1, Vector3f vector2 ) {
		return new Vector3f( vector1.x() + vector2.x(), vector1.y() + vector2.y(), vector1.z() + vector2.z() );
	}

	/** Returns sum of two double vectors. */
	public static Vector3d add( Vector3d vector1, Vector3d vector2 ) {
		return new Vector3d( vector1.x + vector2.x, vector1.y + vector2.y, vector1.z + vector2.z );
	}

	/** Returns sum of two double vectors. */
	public static Vec3 add( Vec3 vector1, Vec3 vector2 ) {
		return new Vec3( vector1.x + vector2.x, vector1.y + vector2.y, vector1.z + vector2.z );
	}

	/** Returns vector increased by given value. */
	public static < Type extends Number > Vec3 add( Vec3 vector, Type value ) {
		return new Vec3( vector.x + ( double )value, vector.y + ( double )value, vector.z + ( double )value );
	}

	/** Returns negation of float vector. */
	public static Vector3f negate( Vector3f vector ) {
		return new Vector3f( -vector.x(), -vector.y(), -vector.z() );
	}

	/** Returns negation of double vector. */
	public static Vector3d negate( Vector3d vector ) {
		return new Vector3d( -vector.x, -vector.y, -vector.z );
	}

	/** Returns negation of double vector. */
	public static Vec3 negate( Vec3 vector ) {
		return new Vec3( -vector.x, -vector.y, -vector.z );
	}

	/** Returns subtraction of two float vectors. */
	public static Vector3f subtract( Vector3f vector1, Vector3f vector2 ) {
		return add( vector1, negate( vector2 ) );
	}

	/** Returns subtraction of two double vectors. */
	public static Vector3d subtract( Vector3d vector1, Vector3d vector2 ) {
		return add( vector1, negate( vector2 ) );
	}

	/** Returns subtraction of two double vectors. */
	public static Vec3 subtract( Vec3 vector1, Vec3 vector2 ) {
		return add( vector1, negate( vector2 ) );
	}

	/** Returns vector decreased by given value. */
	public static < Type extends Number > Vec3 subtract( Vec3 vector, Type value ) {
		return add( vector, -( double )value );
	}

	/** Returns product of two float vectors. */
	public static Vector3f multiply( Vector3f vector1, Vector3f vector2 ) {
		return new Vector3f( vector1.x() * vector2.x(), vector1.y() * vector2.y(), vector1.z() * vector2.z() );
	}

	/** Returns product of two double vectors. */
	public static Vector3d multiply( Vector3d vector1, Vector3d vector2 ) {
		return new Vector3d( vector1.x * vector2.x, vector1.y * vector2.y, vector1.z * vector2.z );
	}

	/** Returns product of two double vectors. */
	public static Vec3 multiply( Vec3 vector1, Vec3 vector2 ) {
		return new Vec3( vector1.x * vector2.x, vector1.y * vector2.y, vector1.z * vector2.z );
	}

	/** Returns vector multiplied by given value. */
	public static < Type extends Number > Vec3 multiply( Vec3 vector, Type value ) {
		return new Vec3( vector.x * ( double )value, vector.y * ( double )value, vector.z * ( double )value );
	}

	/** Returns quotient of two float vectors. */
	public static Vector3f divide( Vector3f vector1, Vector3f vector2 ) {
		return new Vector3f( vector1.x() / vector2.x(), vector1.y() / vector2.y(), vector1.z() / vector2.z() );
	}

	/** Returns quotient of two double vectors. */
	public static Vector3d divide( Vector3d vector1, Vector3d vector2 ) {
		return new Vector3d( vector1.x / vector2.x, vector1.y / vector2.y, vector1.z / vector2.z );
	}

	/** Returns quotient of two double vectors. */
	public static Vec3 divide( Vec3 vector1, Vec3 vector2 ) {
		return new Vec3( vector1.x / vector2.x, vector1.y / vector2.y, vector1.z / vector2.z );
	}

	/** Returns vector divided by given value. */
	public static < Type extends Number > Vec3 divide( Vec3 vector, Type value ) {
		return new Vec3( vector.x / ( double )value, vector.y / ( double )value, vector.z / ( double )value );
	}

	/** Returns scalar product between two float vectors. */
	public static float dot( Vector3f vector1, Vector3f vector2 ) {
		return vector1.dot( vector2 );
	}

	/** Returns scalar product between the same float vector. */
	public static float dot( Vector3f vector ) {
		return dot( vector, vector );
	}

	/** Returns scalar product between two double vectors. */
	public static double dot( Vector3d vector1, Vector3d vector2 ) {
		return vector1.x * vector2.x + vector1.y * vector2.y + vector1.z * vector2.z; // there is not dot function in Vector3d (minecraft 1.17.1, forge 37.0.8)
	}

	/** Returns scalar product between the same double vector. */
	public static double dot( Vector3d vector ) {
		return dot( vector, vector );
	}

	/** Returns scalar product between two double vectors. */
	public static double dot( Vec3 vector1, Vec3 vector2 ) {
		return vector1.dot( vector2 );
	}

	/** Returns scalar product between the same double vector. */
	public static double dot( Vec3 vector ) {
		return dot( vector, vector );
	}

	/** Returns squared distance between two float vectors. */
	public static float distanceSquared( Vector3f vector1, Vector3f vector2 ) {
		return dot( subtract( vector1, vector2 ) );
	}

	/** Returns distance between two float vectors. */
	public static float distance( Vector3f vector1, Vector3f vector2 ) {
		return ( float )Math.sqrt( distanceSquared( vector1, vector2 ) );
	}

	/** Returns length of given float vector. */
	public static float length( Vector3f vector ) {
		return distance( vector, Vector3f.ZERO );
	}

	/** Returns squared distance between two double vectors. */
	public static double distanceSquared( Vector3d vector1, Vector3d vector2 ) {
		return dot( subtract( vector1, vector2 ) );
	}

	/** Returns distance between two double vectors. */
	public static double distance( Vector3d vector1, Vector3d vector2 ) {
		return Math.sqrt( distanceSquared( vector1, vector2 ) );
	}

	/** Returns length of given double vector. */
	public static double length( Vector3d vector ) {
		return distance( vector, new Vector3d( 0.0,  0.0, 0.0 ) );
	}

	/** Returns squared distance between two double vectors. */
	public static double distanceSquared( Vec3 vector1, Vec3 vector2 ) {
		return dot( subtract( vector1, vector2 ) );
	}

	/** Returns distance between two double vectors. */
	public static double distance( Vec3 vector1, Vec3 vector2 ) {
		return Math.sqrt( distanceSquared( vector1, vector2 ) );
	}

	/** Returns length of given double vector. */
	public static double length( Vec3 vector ) {
		return distance( vector, new Vec3( 0.0,  0.0, 0.0 ) );
	}

	/** Returns new normalized float vector. (with distance equal 1) */
	public static Vector3f normalize( Vector3f vector ) {
		float scalarProduct = dot( vector, vector );
		if( scalarProduct < 1e-5 )
			return vector;

		float denominator = Mth.fastInvSqrt( dot( vector, vector ) );
		return divide( vector, new Vector3f( denominator, denominator, denominator ) );
	}

	/** Returns new normalized double vector. (with distance equal 1) */
	public static Vector3d normalize( Vector3d vector ) {
		double scalarProduct = dot( vector, vector );
		if( scalarProduct < 1e-5 )
			return vector;

		double denominator = Mth.fastInvSqrt( dot( vector, vector ) );
		return divide( vector, new Vector3d( denominator, denominator, denominator ) );
	}

	/** Returns new normalized double vector. (with distance equal 1) */
	public static Vec3 normalize( Vec3 vector ) {
		double scalarProduct = dot( vector, vector );
		if( scalarProduct < 1e-5 )
			return vector;

		double denominator = Mth.fastInvSqrt( dot( vector, vector ) );
		return divide( vector, new Vec3( denominator, denominator, denominator ) );
	}
}
