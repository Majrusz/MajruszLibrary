package com.mlib.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class BooleanConfig extends ValueConfig< Boolean, ForgeConfigSpec.BooleanValue > {
	public BooleanConfig( String name, String comment, boolean requiresWorldRestart, boolean defaultValue ) {
		super( name, comment, requiresWorldRestart, defaultValue );
	}

	public boolean isEnabled() {
		return get();
	}

	public boolean isDisabled() {
		return !get();
	}
}
