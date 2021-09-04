package com.mlib.math;

import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

/** Common functions for aligned axis. */
public class AABBHelper {
	/** Returns new inflated aligned axis. */
	public static AABB createInflatedAABB( double x, double y, double z, double offset ) {
		return new AABB( x, y, z, x, y, z ).inflate( offset );
	}

	/** Returns new inflated aligned axis. */
	public static AABB createInflatedAABB( Vec3 position, double offset ) {
		return createInflatedAABB( position.x, position.y, position.z, offset );
	}
}
