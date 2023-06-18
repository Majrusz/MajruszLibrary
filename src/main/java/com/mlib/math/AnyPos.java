package com.mlib.math;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

/**
 This class is an adapter for most position-related operations in the game. It has been
 created because the interface for vector-like classes in Minecraft has changed too many
 times and it is much easier to backport such new class.
 */
public class AnyPos {
	// At the moment it always converts positions to double values. In order to improve
	// the performance in the future it is possible to define subclasses like
	// AnyPosDouble or AnyPosInteger (AnyPos could become an interface), because all
	// methods always return AnyPos and Number types
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

	public AnyPos add( AnyPos pos ) {
		return new AnyPos( this.x + pos.x, this.y + pos.y, this.z + pos.z );
	}

	public AnyPos add( Vec3 vec3 ) {
		return this.add( from( vec3 ) );
	}

	public AnyPos add( Vector3f vec3f ) {
		return this.add( from( vec3f ) );
	}

	public AnyPos add( Vec3i vec3i ) {
		return this.add( from( vec3i ) );
	}

	public AnyPos add( BlockPos blockPos ) {
		return this.add( from( blockPos ) );
	}

	public AnyPos add( Number value ) {
		return this.add( from( value, value, value ) );
	}

	public AnyPos add( Number x, Number y, Number z ) {
		return this.add( from( x, y, z ) );
	}

	public AnyPos sub( AnyPos pos ) {
		return new AnyPos( this.x - pos.x, this.y - pos.y, this.z - pos.z );
	}

	public AnyPos sub( Vec3 vec3 ) {
		return this.sub( from( vec3 ) );
	}

	public AnyPos sub( Vector3f vec3f ) {
		return this.sub( from( vec3f ) );
	}

	public AnyPos sub( Vec3i vec3i ) {
		return this.sub( from( vec3i ) );
	}

	public AnyPos sub( BlockPos blockPos ) {
		return this.sub( from( blockPos ) );
	}

	public AnyPos sub( Number value ) {
		return this.sub( from( value, value, value ) );
	}

	public AnyPos sub( Number x, Number y, Number z ) {
		return this.sub( from( x, y, z ) );
	}

	public AnyPos mul( AnyPos pos ) {
		return new AnyPos( this.x * pos.x, this.y * pos.y, this.z * pos.z );
	}

	public AnyPos mul( Vec3 vec3 ) {
		return this.mul( from( vec3 ) );
	}

	public AnyPos mul( Vector3f vec3f ) {
		return this.mul( from( vec3f ) );
	}

	public AnyPos mul( Vec3i vec3i ) {
		return this.mul( from( vec3i ) );
	}

	public AnyPos mul( BlockPos blockPos ) {
		return this.mul( from( blockPos ) );
	}

	public AnyPos mul( Number value ) {
		return this.mul( from( value, value, value ) );
	}

	public AnyPos mul( Number x, Number y, Number z ) {
		return this.mul( from( x, y, z ) );
	}

	public AnyPos div( AnyPos pos ) {
		return new AnyPos( this.x / pos.x, this.y / pos.y, this.z / pos.z );
	}

	public AnyPos div( Vec3 vec3 ) {
		return this.div( from( vec3 ) );
	}

	public AnyPos div( Vector3f vec3f ) {
		return this.div( from( vec3f ) );
	}

	public AnyPos div( Vec3i vec3i ) {
		return this.div( from( vec3i ) );
	}

	public AnyPos div( BlockPos blockPos ) {
		return this.div( from( blockPos ) );
	}

	public AnyPos div( Number value ) {
		return this.div( from( value, value, value ) );
	}

	public AnyPos div( Number x, Number y, Number z ) {
		return this.div( from( x, y, z ) );
	}

	public Number dot( AnyPos pos ) {
		return this.x * pos.x + this.y * pos.y + this.z * pos.z;
	}

	public Number dot( Vec3 vec3 ) {
		return this.dot( from( vec3 ) );
	}

	public Number dot( Vector3f vec3f ) {
		return this.dot( from( vec3f ) );
	}

	public Number dot( Vec3i vec3i ) {
		return this.dot( from( vec3i ) );
	}

	public Number dot( BlockPos blockPos ) {
		return this.dot( from( blockPos ) );
	}

	public Number dot( Number x, Number y, Number z ) {
		return this.dot( from( x, y, z ) );
	}

	public Number dist( AnyPos pos ) {
		return Math.sqrt( Math.pow( this.x - pos.x, 2 ) + Math.pow( this.y - pos.y, 2 ) + Math.pow( this.z - pos.z, 2 ) );
	}

	public Number dist( Vec3 vec3 ) {
		return this.dist( from( vec3 ) );
	}

	public Number dist( Vector3f vec3f ) {
		return this.dist( from( vec3f ) );
	}

	public Number dist( Vec3i vec3i ) {
		return this.dist( from( vec3i ) );
	}

	public Number dist( BlockPos blockPos ) {
		return this.dist( from( blockPos ) );
	}

	public Number dist( Number x, Number y, Number z ) {
		return this.dist( from( x, y, z ) );
	}

	public Number distSqr( AnyPos pos ) {
		return Math.pow( this.x - pos.x, 2 ) + Math.pow( this.y - pos.y, 2 ) + Math.pow( this.z - pos.z, 2 );
	}

	public Number distSqr( Vec3 vec3 ) {
		return this.distSqr( from( vec3 ) );
	}

	public Number distSqr( Vector3f vec3f ) {
		return this.distSqr( from( vec3f ) );
	}

	public Number distSqr( Vec3i vec3i ) {
		return this.distSqr( from( vec3i ) );
	}

	public Number distSqr( BlockPos blockPos ) {
		return this.distSqr( from( blockPos ) );
	}

	public Number distSqr( Number x, Number y, Number z ) {
		return this.distSqr( from( x, y, z ) );
	}

	public Number dist2d( AnyPos pos ) {
		return Math.sqrt( Math.pow( this.x - pos.x, 2 ) + Math.pow( this.z - pos.z, 2 ) );
	}

	public Number dist2d( Vec3 vec3 ) {
		return this.dist2d( from( vec3 ) );
	}

	public Number dist2d( Vector3f vec3f ) {
		return this.dist2d( from( vec3f ) );
	}

	public Number dist2d( Vec3i vec3i ) {
		return this.dist2d( from( vec3i ) );
	}

	public Number dist2d( BlockPos blockPos ) {
		return this.dist2d( from( blockPos ) );
	}

	public Number dist2d( Number x, Number y, Number z ) {
		return this.dist2d( from( x, y, z ) );
	}

	public AnyPos lerp( float ratio, AnyPos pos ) {
		return new AnyPos( Mth.lerp( ratio, this.x, pos.x ), Mth.lerp( ratio, this.y, pos.y ), Mth.lerp( ratio, this.z, pos.z ) );
	}

	public AnyPos lerp( float ratio, Vec3 vec3 ) {
		return this.lerp( ratio, from( vec3 ) );
	}

	public AnyPos lerp( float ratio, Vector3f vec3f ) {
		return this.lerp( ratio, from( vec3f ) );
	}

	public AnyPos lerp( float ratio, Vec3i vec3i ) {
		return this.lerp( ratio, from( vec3i ) );
	}

	public AnyPos lerp( float ratio, BlockPos blockPos ) {
		return this.lerp( ratio, from( blockPos ) );
	}

	public AnyPos lerp( float ratio, Number x, Number y, Number z ) {
		return this.lerp( ratio, from( x, y, z ) );
	}

	public Number len() {
		return Math.sqrt( Math.pow( this.x, 2 ) + Math.pow( this.y, 2 ) + Math.pow( this.z, 2 ) );
	}

	public AnyPos norm() {
		double length = this.len().doubleValue();

		return length > 1e-5 ? this.div( length ) : this;
	}

	public AnyPos neg() {
		return new AnyPos( -this.x, -this.y, -this.z );
	}

	public AnyPos center() {
		return new AnyPos( Math.floor( this.x ) + 0.5, Math.floor( this.y ) + 0.5, Math.floor( this.z ) + 0.5 );
	}

	public AnyPos floor() {
		return new AnyPos( Math.floor( this.x ), Math.floor( this.y ), Math.floor( this.z ) );
	}

	public AnyPos ceil() {
		return new AnyPos( Math.ceil( this.x ), Math.ceil( this.y ), Math.ceil( this.z ) );
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
}
