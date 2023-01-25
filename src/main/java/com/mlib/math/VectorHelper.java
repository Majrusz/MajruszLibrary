package com.mlib.math;

import org.joml.Vector3f;
import org.joml.Vector3d;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

/**
 Class with few functions for common vector operations.
 Added because there were not enough functions in Minecraft 1.17.1.
 */
public class VectorHelper {
	public static Vector3f add( Vector3f vector1, Vector3f vector2 ) {
		return new Vector3f( vector1.x() + vector2.x(), vector1.y() + vector2.y(), vector1.z() + vector2.z() );
	}

	public static Vector3d add( Vector3d vector1, Vector3d vector2 ) {
		return new Vector3d( vector1.x + vector2.x, vector1.y + vector2.y, vector1.z + vector2.z );
	}

	public static Vec3 add( Vec3 vector1, Vec3 vector2 ) {
		return new Vec3( vector1.x + vector2.x, vector1.y + vector2.y, vector1.z + vector2.z );
	}

	public static < Type extends Number > Vec3 add( Vec3 vector, Type value ) {
		return new Vec3( vector.x + value.doubleValue(), vector.y + value.doubleValue(), vector.z + value.doubleValue() );
	}

	public static Vector3f negate( Vector3f vector ) {
		return new Vector3f( -vector.x(), -vector.y(), -vector.z() );
	}

	public static Vector3d negate( Vector3d vector ) {
		return new Vector3d( -vector.x, -vector.y, -vector.z );
	}

	public static Vec3 negate( Vec3 vector ) {
		return new Vec3( -vector.x, -vector.y, -vector.z );
	}

	public static Vector3f subtract( Vector3f vector1, Vector3f vector2 ) {
		return add( vector1, negate( vector2 ) );
	}

	public static Vector3d subtract( Vector3d vector1, Vector3d vector2 ) {
		return add( vector1, negate( vector2 ) );
	}

	public static Vec3 subtract( Vec3 vector1, Vec3 vector2 ) {
		return add( vector1, negate( vector2 ) );
	}

	public static < Type extends Number > Vec3 subtract( Vec3 vector, Type value ) {
		return add( vector, -value.doubleValue() );
	}

	public static Vector3f multiply( Vector3f vector1, Vector3f vector2 ) {
		return new Vector3f( vector1.x() * vector2.x(), vector1.y() * vector2.y(), vector1.z() * vector2.z() );
	}

	public static Vector3d multiply( Vector3d vector1, Vector3d vector2 ) {
		return new Vector3d( vector1.x * vector2.x, vector1.y * vector2.y, vector1.z * vector2.z );
	}

	public static Vec3 multiply( Vec3 vector1, Vec3 vector2 ) {
		return new Vec3( vector1.x * vector2.x, vector1.y * vector2.y, vector1.z * vector2.z );
	}

	public static < Type extends Number > Vec3 multiply( Vec3 vector, Type value ) {
		return new Vec3( vector.x * value.doubleValue(), vector.y * value.doubleValue(), vector.z * value.doubleValue() );
	}

	public static Vector3f divide( Vector3f vector1, Vector3f vector2 ) {
		return new Vector3f( vector1.x() / vector2.x(), vector1.y() / vector2.y(), vector1.z() / vector2.z() );
	}

	public static Vector3d divide( Vector3d vector1, Vector3d vector2 ) {
		return new Vector3d( vector1.x / vector2.x, vector1.y / vector2.y, vector1.z / vector2.z );
	}

	public static Vec3 divide( Vec3 vector1, Vec3 vector2 ) {
		return new Vec3( vector1.x / vector2.x, vector1.y / vector2.y, vector1.z / vector2.z );
	}

	public static < Type extends Number > Vec3 divide( Vec3 vector, Type value ) {
		return new Vec3( vector.x / value.doubleValue(), vector.y / value.doubleValue(), vector.z / value.doubleValue() );
	}

	public static float dot( Vector3f vector1, Vector3f vector2 ) {
		return vector1.dot( vector2 );
	}

	public static float dot( Vector3f vector ) {
		return dot( vector, vector );
	}

	public static double dot( Vector3d vector1, Vector3d vector2 ) {
		return vector1.x * vector2.x + vector1.y * vector2.y + vector1.z * vector2.z; // there was no dot function for Vector3d (minecraft 1.17.1, forge 37.0.8)
	}

	public static double dot( Vector3d vector ) {
		return dot( vector, vector );
	}

	public static double dot( Vec3 vector1, Vec3 vector2 ) {
		return vector1.dot( vector2 );
	}

	public static double dot( Vec3 vector ) {
		return dot( vector, vector );
	}

	public static float distanceSquared( Vector3f vector1, Vector3f vector2 ) {
		return dot( subtract( vector1, vector2 ) );
	}

	public static float distance( Vector3f vector1, Vector3f vector2 ) {
		return ( float )Math.sqrt( distanceSquared( vector1, vector2 ) );
	}

	public static float distanceHorizontal( Vector3f vector1, Vector3f vector2 ) {
		return distance( new Vector3f( vector1.x, 0.0f, vector1.z ), new Vector3f( vector2.x, 0.0f, vector2.z ) );
	}

	public static float length( Vector3f vector ) {
		return distance( vector, new Vector3f( 0.0f, 0.0f, 0.0f ) );
	}

	public static double distanceSquared( Vector3d vector1, Vector3d vector2 ) {
		return dot( subtract( vector1, vector2 ) );
	}

	public static double distance( Vector3d vector1, Vector3d vector2 ) {
		return Math.sqrt( distanceSquared( vector1, vector2 ) );
	}

	public static double distanceHorizontal( Vector3d vector1, Vector3d vector2 ) {
		return distance( new Vector3d( vector1.x, 0.0, vector1.z ), new Vector3d( vector2.x, 0.0, vector2.z ) );
	}

	public static double length( Vector3d vector ) {
		return distance( vector, new Vector3d( 0.0, 0.0, 0.0 ) );
	}

	public static double distanceSquared( Vec3 vector1, Vec3 vector2 ) {
		return dot( subtract( vector1, vector2 ) );
	}

	public static double distance( Vec3 vector1, Vec3 vector2 ) {
		return Math.sqrt( distanceSquared( vector1, vector2 ) );
	}

	public static double distanceHorizontal( Vec3 vector1, Vec3 vector2 ) {
		return distance( new Vec3( vector1.x, 0.0, vector1.z ), new Vec3( vector2.x, 0.0, vector2.z ) );
	}

	public static double length( Vec3 vector ) {
		return distance( vector, new Vec3( 0.0, 0.0, 0.0 ) );
	}

	public static Vector3f normalize( Vector3f vector ) {
		float length = length( vector );

		return length < 1e-4 ? vector : divide( vector, new Vector3f( length, length, length ) );
	}

	public static Vector3d normalize( Vector3d vector ) {
		double length = length( vector );

		return length < 1e-4 ? vector : divide( vector, new Vector3d( length, length, length ) );
	}

	public static Vec3 normalize( Vec3 vector ) {
		double length = length( vector );

		return length < 1e-4 ? vector : divide( vector, new Vec3( length, length, length ) );
	}

	public static Vector3f lerp( float ratio, Vector3f vector1, Vector3f vector2 ) {
		float x = Mth.lerp( ratio, vector1.x(), vector2.x() );
		float y = Mth.lerp( ratio, vector1.y(), vector2.y() );
		float z = Mth.lerp( ratio, vector1.z(), vector2.z() );

		return new Vector3f( x, y, z );
	}

	public static Vector3d lerp( double ratio, Vector3d vector1, Vector3d vector2 ) {
		double x = Mth.lerp( ratio, vector1.x, vector2.x );
		double y = Mth.lerp( ratio, vector1.y, vector2.y );
		double z = Mth.lerp( ratio, vector1.z, vector2.z );

		return new Vector3d( x, y, z );
	}

	public static Vec3 lerp( double ratio, Vec3 vector1, Vec3 vector2 ) {
		double x = Mth.lerp( ratio, vector1.x, vector2.x );
		double y = Mth.lerp( ratio, vector1.y, vector2.y );
		double z = Mth.lerp( ratio, vector1.z, vector2.z );

		return new Vec3( x, y, z );
	}

	public static BlockPos block( Vec3 vector ) {
		return new BlockPos( vector.x, vector.y, vector.z );
	}

	public static BlockPos block( Vector3f vector ) {
		return new BlockPos( vector.x(), vector.y(), vector.z() );
	}

	public static BlockPos block( Vector3d vector ) {
		return new BlockPos( vector.x, vector.y, vector.z );
	}

	public static Vec3 vec3( BlockPos blockPosition ) {
		return new Vec3( blockPosition.getX() + 0.5, blockPosition.getY() + 0.5, blockPosition.getZ() + 0.5 );
	}

	public static Vec3 vec3( Vector3f vector ) {
		return new Vec3( vector.x(), vector.y(), vector.z() );
	}

	public static Vec3 vec3( Vector3d vector ) {
		return new Vec3( vector.x, vector.y, vector.z );
	}

	public static Vector3f vec3f( BlockPos blockPosition ) {
		return new Vector3f( blockPosition.getX() + 0.5f, blockPosition.getY() + 0.5f, blockPosition.getZ() + 0.5f );
	}

	public static Vector3f vec3f( Vec3 vector ) {
		return new Vector3f( ( float )vector.x, ( float )vector.y, ( float )vector.z );
	}

	public static Vector3f vec3f( Vector3d vector ) {
		return new Vector3f( ( float )vector.x, ( float )vector.y, ( float )vector.z );
	}

	public static Vector3d vec3d( BlockPos blockPosition ) {
		return new Vector3d( blockPosition.getX() + 0.5, blockPosition.getY() + 0.5, blockPosition.getZ() + 0.5 );
	}

	public static Vector3d vec3d( Vec3 vector ) {
		return new Vector3d( vector.x, vector.y, vector.z );
	}

	public static Vector3d vec3d( Vector3f vector ) {
		return new Vector3d( vector.x(), vector.y(), vector.z() );
	}
}
