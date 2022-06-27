package com.mlib.config;

import net.minecraftforge.common.ForgeConfigSpec;

public abstract class NumberConfig< Type extends Number, ConfigType extends ForgeConfigSpec.ConfigValue< Type > > extends ValueConfig< Type, ConfigType > {
	protected final Type min;
	protected final Type max;

	public NumberConfig( String name, String comment, boolean requiresWorldRestart, Type defaultValue, Type min, Type max ) {
		super( name, comment, requiresWorldRestart, defaultValue );
		this.min = min;
		this.max = max;
	}
}
