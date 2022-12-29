package com.mlib.math;

public class Range< Type extends Number & Comparable< Type > > {
	public static Range< Double > CHANCE = new Range<>( 0.0, 1.0 );
	public Type from;
	public Type to;

	public Range( Type from, Type to ) {
		this.from = from;
		this.to = to;
	}

	public boolean within( Type value ) {
		return this.from.compareTo( value ) <= 0 && this.to.compareTo( value ) >= 0;
	}
}
