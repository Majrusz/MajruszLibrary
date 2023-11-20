package com.majruszlibrary.math;

import org.joml.Quaterniond;

public class AnyRot {
	Quaterniond quaternion;

	public static AnyRot zero() {
		return new AnyRot( new Quaterniond() );
	}

	public static AnyRot x( double radians ) {
		return zero().rotX( radians );
	}

	public static AnyRot y( double radians ) {
		return zero().rotY( radians );
	}

	public static AnyRot z( double radians ) {
		return zero().rotZ( radians );
	}

	public AnyRot rotX( double radians ) {
		return new AnyRot( new Quaterniond( this.quaternion ).rotateX( radians ) );
	}

	public AnyRot rotY( double radians ) {
		return new AnyRot( new Quaterniond( this.quaternion ).rotateY( radians ) );
	}

	public AnyRot rotZ( double radians ) {
		return new AnyRot( new Quaterniond( this.quaternion ).rotateZ( radians ) );
	}

	private AnyRot( Quaterniond quaternion ) {
		this.quaternion = quaternion;
	}
}
