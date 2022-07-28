package com.mlib.config;

public abstract class NumberConfig< Type extends Number > extends ValueConfig< Type > {
	protected final Type min;
	protected final Type max;

	public NumberConfig( String name, String comment, boolean requiresWorldRestart, Type defaultValue, Type min, Type max ) {
		super( name, comment, requiresWorldRestart, defaultValue );
		this.min = min;
		this.max = max;
	}
}
