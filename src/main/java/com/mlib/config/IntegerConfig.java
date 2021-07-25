package com.mlib.config;

import net.minecraftforge.common.ForgeConfigSpec;

/** Class representing single integer config value. */
public class IntegerConfig extends BaseConfig< Integer > {
	public ForgeConfigSpec.IntValue intValue;
	protected final int defaultValue;
	protected final int minimum;
	protected final int maximum;

	public IntegerConfig( String name, String comment, boolean requiresWorldRestart, int defaultValue, int minimum, int maximum ) {
		super( name, comment, requiresWorldRestart );
		this.defaultValue = defaultValue;
		this.minimum = minimum;
		this.maximum = maximum;
	}

	/** Returns value directly stored in a config. */
	public Integer get() {
		return this.intValue.get();
	}

	/** Builds current config. */
	@Override
	public void build( ForgeConfigSpec.Builder builder ) {
		super.build( builder );

		this.intValue = builder.defineInRange( this.name, this.defaultValue, this.minimum, this.maximum );
	}
}
