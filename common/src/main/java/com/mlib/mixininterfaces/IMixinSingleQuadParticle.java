package com.mlib.mixininterfaces;

import org.joml.Quaternionf;

public interface IMixinSingleQuadParticle {
	float getY( float y );

	Quaternionf getQuaternion( Quaternionf quaternion );
}
