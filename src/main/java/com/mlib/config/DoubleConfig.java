package com.mlib.config;

import com.mlib.Utility;
import net.minecraftforge.common.ForgeConfigSpec;

public class DoubleConfig extends NumberConfig< Double, ForgeConfigSpec.DoubleValue > {
	public DoubleConfig( String name, String comment, boolean requiresWorldRestart, double defaultValue, double min, double max ) {
		super( name, comment, requiresWorldRestart, defaultValue, min, max );
	}

	public int asTicks() {
		return Utility.secondsToTicks( super.get() );
	}

	public float asFloat() {
		return super.get().floatValue();
	}

	@Override
	public ForgeConfigSpec.DoubleValue buildValue( ForgeConfigSpec.Builder builder ) {
		return builder.defineInRange( this.name, this.defaultValue, this.min, this.max );
	}
}
