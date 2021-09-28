package com.mlib.animations;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class FrameVec3 extends Frame< Vec3 > {
	public FrameVec3( float startDuration, Vec3 value, InterpolationType interpolationType ) {
		super( startDuration, value, interpolationType );
	}

	public FrameVec3( float startDuration, Vec3 value ) {
		super( startDuration, value );
	}

	public FrameVec3( float startDuration, double x, double y, double z, InterpolationType interpolationType ) {
		this( startDuration, new Vec3( x, y, z ), interpolationType );
	}

	public FrameVec3( float startDuration, double x, double y, double z ) {
		this( startDuration, new Vec3( x, y, z ) );
	}

	@Override
	public Vec3 interpolate( float ratio, Frame< Vec3 > nextFrame ) {
		double x = Mth.lerp( ratio, getValue().x, nextFrame.getValue().x );
		double y = Mth.lerp( ratio, getValue().y, nextFrame.getValue().y );
		double z = Mth.lerp( ratio, getValue().z, nextFrame.getValue().z );

		return new Vec3( x, y, z );
	}
}
