package com.mlib.config;

import com.mlib.math.Range;
import net.minecraftforge.common.ForgeConfigSpec;

public class IntegerConfig extends NumberConfig< Integer > {
	public IntegerConfig( int defaultValue, Range< Integer > range ) {
		super( defaultValue, range );
	}

	@Override
	public void build( ForgeConfigSpec.Builder builder ) {
		super.build( builder );

		this.config = builder.defineInRange( this.name, this.defaultValue, this.range.from, this.range.to );
	}
}

