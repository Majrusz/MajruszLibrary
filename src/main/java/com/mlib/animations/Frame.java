package com.mlib.animations;

import com.mlib.math.AnyPos;
import com.mlib.math.VectorHelper;
import net.minecraft.util.Mth;
import org.joml.Vector3f;

public abstract class Frame< Type > {
	final Type value;
	final InterpolationType interpolationType;

	public Frame( Type value, InterpolationType interpolationType ) {
		this.value = value;
		this.interpolationType = interpolationType;
	}

	public Frame( Type value ) {
		this( value, InterpolationType.LINEAR );
	}

	public abstract Type interpolate( float ratio, Frame< Type > nextFrame );

	public Type getValue() {
		return this.value;
	}

	public static class Value extends Frame< Float > {
		public Value( float value, InterpolationType interpolationType ) {
			super( value, interpolationType );
		}

		public Value( float value ) {
			super( value );
		}

		@Override
		public Float interpolate( float ratio, Frame< Float > nextFrame ) {
			return Mth.lerp( nextFrame.interpolationType.apply( ratio ), this.getValue(), nextFrame.getValue() );
		}
	}

	public static class Degrees extends Value {
		public Degrees( float value, InterpolationType interpolationType ) {
			super( value, interpolationType );
		}

		public Degrees( float value ) {
			super( value );
		}

		@Override
		public Float getValue() {
			return Mth.DEG_TO_RAD * this.value;
		}
	}

	public static class Vector extends Frame< Vector3f > {
		public Vector( Vector3f value, InterpolationType interpolationType ) {
			super( value, interpolationType );
		}

		public Vector( Vector3f value ) {
			super( value );
		}

		public Vector( float x, float y, float z, InterpolationType interpolationType ) {
			this( new Vector3f( x, y, z ), interpolationType );
		}

		public Vector( float x, float y, float z ) {
			this( new Vector3f( x, y, z ) );
		}


		@Override
		public Vector3f interpolate( float ratio, Frame< Vector3f > nextFrame ) {
			return AnyPos.from( this.getValue() ).lerp( nextFrame.interpolationType.apply( ratio ), nextFrame.getValue() ).vec3f();
		}
	}
}
