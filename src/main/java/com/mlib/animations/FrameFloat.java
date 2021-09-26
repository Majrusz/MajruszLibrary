package com.mlib.animations;

import net.minecraft.util.Mth;

public class FrameFloat extends Frame< Float > {
	public FrameFloat( float startDuration, float value, InterpolationType interpolationType ) {
		super( startDuration, value, interpolationType );
	}

	public FrameFloat( float startDuration, float value ) {
		super( startDuration, value );
	}

	@Override
	public Float interpolate( float ratio, Frame< Float > nextFrame ) {
		return Mth.lerp( ratio, getValue(), nextFrame.getValue() );
	}
}
