package com.mlib.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class BooleanConfig extends ValueConfig< Boolean > {
	public BooleanConfig( boolean defaultValue ) {
		super( defaultValue );
	}

	@Override
	public void build( ForgeConfigSpec.Builder builder ) {
		super.build( builder );

		this.config = builder.define( this.name, this.defaultValue );
	}

	public boolean isEnabled() {
		return get();
	}

	public boolean isDisabled() {
		return !get();
	}
}
