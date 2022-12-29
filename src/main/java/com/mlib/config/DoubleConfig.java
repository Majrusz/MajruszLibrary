package com.mlib.config;

import com.mlib.Utility;
import com.mlib.math.Range;
import net.minecraftforge.common.ForgeConfigSpec;

public class DoubleConfig extends NumberConfig< Double > {
	public DoubleConfig( double defaultValue, Range< Double > range ) {
		super( defaultValue, range );
	}

	@Override
	public void build( ForgeConfigSpec.Builder builder ) {
		super.build( builder );

		this.config = builder.defineInRange( this.name, this.defaultValue, this.range.from, this.range.to );
	}

	public int asTicks() {
		return Utility.secondsToTicks( super.get() );
	}

	public float asFloat() {
		return super.get().floatValue();
	}
}
