package com.mlib.config;

import net.minecraftforge.common.ForgeConfigSpec;

/** Class representing single boolean config value. */
public class AvailabilityConfig extends BaseConfig {
	public ForgeConfigSpec.BooleanValue booleanValue;
	protected final boolean defaultValue;

	public AvailabilityConfig( String name, String comment, boolean requiresWorldRestart, boolean defaultValue ) {
		super( name, comment, requiresWorldRestart );
		this.defaultValue = defaultValue;
	}

	/** Checks if a value from the file is true. */
	public boolean isEnabled() {
		return this.booleanValue.get();
	}

	/** Checks if a value from the file is false. */
	public boolean isDisabled() {
		return !this.booleanValue.get();
	}

	/** Builds current config. */
	@Override
	public void build( ForgeConfigSpec.Builder builder ) {
		super.build( builder );

		this.booleanValue = builder.define( this.name, this.defaultValue );
	}
}
