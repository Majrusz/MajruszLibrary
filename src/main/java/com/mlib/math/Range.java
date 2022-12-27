package com.mlib.math;

public class Range< Type extends Number > {
	public Type from;
	public Type to;

	public Range( Type from, Type to ) {
		this.from = from;
		this.to = to;
	}
}
