package com.majruszlibrary.math;

import com.mojang.math.Quaternion;

public class AnyRot {
	Quaternion quaternion;

	public static AnyRot zero() {
		return new AnyRot( Quaternion.ONE );
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
		float sin = ( float )Math.sin( radians * 0.5 );
		float cos = ( float )Math.cos( radians * 0.5 );
		Quaternion quaternion = new Quaternion( this.quaternion );
		quaternion.set( this.quaternion.r() * sin + this.quaternion.i() * cos,
			this.quaternion.j() * cos + this.quaternion.k() * sin,
			this.quaternion.k() * cos - this.quaternion.j() * sin,
			this.quaternion.r() * cos - this.quaternion.i() * sin
		);

		return new AnyRot( quaternion );
	}

	public AnyRot rotY( double radians ) {
		float sin = ( float )Math.sin( radians * 0.5 );
		float cos = ( float )Math.cos( radians * 0.5 );
		Quaternion quaternion = new Quaternion( this.quaternion );
		quaternion.set( this.quaternion.i() * cos - this.quaternion.k() * sin,
			this.quaternion.r() * sin + this.quaternion.j() * cos,
			this.quaternion.i() * sin + this.quaternion.k() * cos,
			this.quaternion.r() * cos - this.quaternion.j() * sin
		);

		return new AnyRot( quaternion );
	}

	public AnyRot rotZ( double radians ) {
		float sin = ( float )Math.sin( radians * 0.5 );
		float cos = ( float )Math.cos( radians * 0.5 );
		Quaternion quaternion = new Quaternion( this.quaternion );
		quaternion.set( this.quaternion.i() * cos + this.quaternion.j() * sin,
			this.quaternion.j() * cos - this.quaternion.i() * sin,
			this.quaternion.r() * sin + this.quaternion.k() * cos,
			this.quaternion.r() * cos - this.quaternion.k() * sin
		);

		return new AnyRot( quaternion );
	}

	private AnyRot( Quaternion quaternion ) {
		this.quaternion = quaternion;
	}
}
