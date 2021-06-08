package com.mlib.config;

import net.minecraftforge.common.ForgeConfigSpec;

/** Class representing single boolean config value. */
public class AvailabilityConfig extends BaseConfig< Boolean > {
	public ForgeConfigSpec.BooleanValue booleanValue;
	protected final boolean defaultValue;

	public AvailabilityConfig( String name, String comment, boolean requiresWorldRestart, boolean defaultValue ) {
		super( name, comment, requiresWorldRestart );
		this.defaultValue = defaultValue;
	}

	/** Checks if a value from the config is true. */
	public boolean isEnabled() {
		return get();
	}

	/** Checks if a value from the config is false. */
	public boolean isDisabled() {
		return !get();
	}

	/** Returns value directly stored in a config. */
	public Boolean get() {
		return this.booleanValue.get();
	}

	/** Builds current config. */
	@Override
	public void build( ForgeConfigSpec.Builder builder ) {
		super.build( builder );

		this.booleanValue = builder.define( this.name, this.defaultValue );
	}
}
