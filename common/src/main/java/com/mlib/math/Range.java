package com.mlib.math;

import net.minecraft.util.Mth;

import java.util.function.Predicate;

public class Range< Type extends Number & Comparable< Type > > implements Predicate< Type > {
	public static Range< Float > CHANCE = Range.of( 0.0f, 1.0f );
	public Type from;
	public Type to;

	public static < Type extends Number & Comparable< Type > > Range< Type > of( Type from, Type to ) {
		return new Range<>( from, to );
	}

	public static < Type extends Number & Comparable< Type > > Range< Type > validated( Type from, Type to ) {
		return new Range<>( to.compareTo( from ) < 0 ? to : from, to );
	}

	@Override
	public String toString() {
		return String.format( "%s ~ %s", this.from, this.to );
	}

	@Override
	public boolean test( Type value ) {
		return this.within( value );
	}

	public boolean within( Type value ) {
		return this.from.compareTo( value ) <= 0 && this.to.compareTo( value ) >= 0;
	}

	public Type clamp( Type value ) {
		return this.from.compareTo( value ) > 0 ? this.from : ( this.to.compareTo( value ) < 0 ? this.to : value );
	}

	public Range< Type > clamp( Range< Type > value ) {
		return Range.of( this.clamp( value.from ), this.clamp( value.to ) );
	}

	public float lerp( float ratio ) {
		return Mth.lerp( ratio, this.from.floatValue(), this.to.floatValue() );
	}

	public double lerp( double ratio ) {
		return Mth.lerp( ratio, this.from.doubleValue(), this.to.doubleValue() );
	}

	private Range( Type from, Type to ) {
		this.from = from;
		this.to = to;
	}
}
