package com.mlib.config;

import com.mlib.Utility;
import net.minecraftforge.common.ForgeConfigSpec;

public class DoubleConfig extends NumberConfig< Double > {
	public DoubleConfig( String name, String comment, boolean worldRestartRequired, double defaultValue, double min, double max ) {
		super( name, comment, worldRestartRequired, defaultValue, min, max );
	}

	@Override
	public void build( ForgeConfigSpec.Builder builder ) {
		super.build( builder );

		this.config = builder.defineInRange( this.name, this.defaultValue, this.min, this.max );
	}

	public int asTicks() {
		return Utility.secondsToTicks( super.get() );
	}

	public float asFloat() {
		return super.get().floatValue();
	}
}
