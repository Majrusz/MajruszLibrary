package com.mlib.animations;

import net.minecraft.util.Mth;

public class FrameDegrees extends FrameFloat {
	public FrameDegrees( float startDuration, float value, InterpolationType interpolationType ) {
		super( startDuration, value, interpolationType );
	}

	public FrameDegrees( float startDuration, float value ) {
		super( startDuration, value );
	}

	@Override
	public Float getValue() {
		return Mth.DEG_TO_RAD * this.value;
	}
}
