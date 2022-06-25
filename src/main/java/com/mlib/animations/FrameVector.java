package com.mlib.animations;

import com.mlib.math.VectorHelper;
import com.mojang.math.Vector3f;

public class FrameVector extends Frame< Vector3f > {
	public FrameVector( float startDuration, Vector3f value, InterpolationType interpolationType ) {
		super( startDuration, value, interpolationType );
	}

	public FrameVector( float startDuration, Vector3f value ) {
		super( startDuration, value );
	}

	@Override
	public Vector3f interpolate( float ratio, Frame< Vector3f > nextFrame ) {
		return VectorHelper.lerp( ratio, getValue(), nextFrame.getValue() );
	}
}
