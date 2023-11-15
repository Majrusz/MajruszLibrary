package com.mlib.animations;

import org.joml.Vector2i;
import org.joml.Vector3f;

import java.util.List;

public class AnimationHelper {
	public static Vector3f to3d( List< Float > values ) {
		return new Vector3f( values.get( 0 ), values.get( 1 ), values.get( 2 ) );
	}

	public static List< Float > to3d( Vector3f vector ) {
		return List.of( vector.x, vector.y, vector.z );
	}

	public static Vector3f toRadians3d( List< Float > values ) {
		float scale = ( float )Math.PI / 180.0f;

		return new Vector3f( scale * values.get( 0 ), scale * values.get( 1 ), scale * values.get( 2 ) );
	}

	public static List< Float > toRadians3d( Vector3f vector ) {
		float scale = 180.0f * ( float )Math.PI;

		return List.of( scale * vector.x, scale * vector.y, scale * vector.z );
	}

	public static Vector2i to2d( List< Integer > values ) {
		return new Vector2i( values.get( 0 ), values.get( 1 ) );
	}

	public static List< Integer > to2d( Vector2i vector ) {
		return List.of( vector.x, vector.y );
	}
}
