package net.mlib.math;

import net.minecraft.util.Mth;

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

	public Type clamp( Type value ) {
		return this.from.compareTo( value ) > 0 ? this.from : ( this.to.compareTo( value ) < 0 ? this.to : value );
	}

	public float lerp( float ratio ) {
		return Mth.lerp( ratio, this.from.floatValue(), this.to.floatValue() );
	}

	public double lerp( double ratio ) {
		return Mth.lerp( ratio, this.from.doubleValue(), this.to.doubleValue() );
	}

	@Override
	public String toString() {
		return String.format( "%s ~ %s", this.from, this.to );
	}
}
