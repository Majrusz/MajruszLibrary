package com.mlib.config;

import net.minecraftforge.common.ForgeConfigSpec;

/** Class representing single double config value. */
public class DoubleConfig extends BaseConfig< Double > {
	public ForgeConfigSpec.DoubleValue doubleValue;
	protected final double defaultValue;
	protected final double minimum;
	protected final double maximum;

	public DoubleConfig( String name, String comment, boolean requiresWorldRestart, double defaultValue, double minimum, double maximum ) {
		super( name, comment, requiresWorldRestart );
		this.defaultValue = defaultValue;
		this.minimum = minimum;
		this.maximum = maximum;
	}

	/** Returns value directly stored in a config. */
	public Double get() {
		return this.doubleValue.get();
	}

	/** Builds current config. */
	@Override
	public void build( ForgeConfigSpec.Builder builder ) {
		super.build( builder );

		this.doubleValue = builder.defineInRange( this.name, this.defaultValue, this.minimum, this.maximum );
	}
}
