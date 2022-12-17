package com.mlib.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.function.Supplier;

public abstract class ValueConfig< Type > extends UserConfig implements Supplier< Type > {
	protected final Type defaultValue;
	protected ForgeConfigSpec.ConfigValue< Type > config = null;

	public ValueConfig( String name, String comment, boolean worldRestartRequired, Type defaultValue ) {
		super( name, comment, worldRestartRequired );
		this.defaultValue = defaultValue;
	}

	/** Returns stored (or cached) value. */
	@Override
	public Type get() {
		assert this.config != null : "Config has not been initialized yet!";
		return this.config.get();
	}

	@Override
	public boolean isBuilt() {
		return this.config != null;
	}

	public Type getOrDefault() {
		return this.config != null ? this.config.get() : this.defaultValue;
	}
}
