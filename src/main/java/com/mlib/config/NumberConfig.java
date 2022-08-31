package com.mlib.config;

public abstract class NumberConfig< Type extends Number > extends ValueConfig< Type > {
	protected final Type min;
	protected final Type max;

	public NumberConfig( String name, String comment, boolean worldRestartRequired, Type defaultValue, Type min, Type max ) {
		super( name, comment, worldRestartRequired, defaultValue );
		this.min = min;
		this.max = max;
	}
}
