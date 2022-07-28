package com.mlib.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class IntegerConfig extends NumberConfig< Integer > {
	public IntegerConfig( String name, String comment, boolean requiresWorldRestart, int defaultValue, int min, int max ) {
		super( name, comment, requiresWorldRestart, defaultValue, min, max );
	}

	@Override
	public void build( ForgeConfigSpec.Builder builder ) {
		super.build( builder );

		this.config = builder.defineInRange( this.name, this.defaultValue, this.min, this.max );
	}
}

