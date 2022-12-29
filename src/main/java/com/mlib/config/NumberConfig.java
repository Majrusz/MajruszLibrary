package com.mlib.config;

import com.mlib.math.Range;

public abstract class NumberConfig< Type extends Number & Comparable< Type > > extends ValueConfig< Type > {
	protected final Range< Type > range;

	public NumberConfig( Type defaultValue, Range< Type > range ) {
		super( defaultValue );
		this.range = range;
	}
}
