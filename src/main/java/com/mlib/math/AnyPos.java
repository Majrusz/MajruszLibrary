package com.mlib.math;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class AnyPos {
	double x, y, z;

	public static AnyPos from( Vec3 vec3 ) {
		return new AnyPos( vec3.x, vec3.y, vec3.z );
	}

	public static AnyPos from( Vector3f vec3f ) {
		return new AnyPos( vec3f.x, vec3f.y, vec3f.z );
	}

	public static AnyPos from( Vec3i vec3i ) {
		return new AnyPos( vec3i.getX(), vec3i.getY(), vec3i.getZ() );
	}

	public static AnyPos from( BlockPos blockPos ) {
		return new AnyPos( blockPos.getX(), blockPos.getY(), blockPos.getZ() );
	}

	public static AnyPos from( Number x, Number y, Number z ) {
		return new AnyPos( x.doubleValue(), y.doubleValue(), z.doubleValue() );
	}

	private AnyPos( double x, double y, double z ) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public AnyPos add( Vec3 vec3 ) {
		return add( this, from( vec3 ) );
	}

	public AnyPos add( Vector3f vec3f ) {
		return add( this, from( vec3f ) );
	}

	public AnyPos add( Vec3i vec3i ) {
		return add( this, from( vec3i ) );
	}

	public AnyPos add( BlockPos blockPos ) {
		return add( this, from( blockPos ) );
	}

	public AnyPos add( Number value ) {
		return add( this, from( value, value, value ) );
	}

	public AnyPos add( Number x, Number y, Number z ) {
		return add( this, from( x, y, z ) );
	}

	public Vec3 vec3() {
		return new Vec3( this.x, this.y, this.z );
	}

	public Vector3f vec3f() {
		return new Vector3f( ( float )this.x, ( float )this.y, ( float )this.z );
	}

	public Vec3i vec3i() {
		return new Vec3i( ( int )this.x, ( int )this.y, ( int )this.z );
	}

	public BlockPos block() {
		return new BlockPos( ( int )this.x, ( int )this.y, ( int )this.z );
	}

	private static AnyPos add( AnyPos pos1, AnyPos pos2 ) {
		return new AnyPos( pos1.x + pos2.x, pos1.y + pos2.y, pos1.z + pos2.z );
	}
}
