package com.mlib.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class IntegerConfig extends NumberConfig< Integer, ForgeConfigSpec.IntValue > {
	public IntegerConfig( String name, String comment, boolean requiresWorldRestart, int defaultValue, int min, int max ) {
		super( name, comment, requiresWorldRestart, defaultValue, min, max );
	}

	@Override
	public ForgeConfigSpec.IntValue buildValue( ForgeConfigSpec.Builder builder ) {
		return builder.defineInRange( this.name, this.defaultValue, this.min, this.max );
	}
}

