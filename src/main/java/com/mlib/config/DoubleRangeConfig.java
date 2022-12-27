package com.mlib.config;

import com.mlib.math.Range;
import net.minecraft.util.Mth;
import net.minecraftforge.common.ForgeConfigSpec;

public class DoubleRangeConfig extends RangeConfig< Double > {
	public DoubleRangeConfig( String name, String comment, boolean worldRestartRequired, Range< Double > defaultValue, double min,
		double max
	) {
		super( name, comment, worldRestartRequired, defaultValue, min, max );
	}

	@Override
	public void build( ForgeConfigSpec.Builder builder ) {
		super.build( builder );

		builder.push( this.name );
		this.configFrom = builder.define( "from", this.defaultValue.from, this.predicateFrom );
		this.configTo = builder.define( "to", this.defaultValue.to, this.predicateTo );
		builder.pop();
	}

	public float lerp( float ratio ) {
		Range< Double > range = this.getOrDefault();

		return ( float )Mth.lerp( ratio, range.from, range.to );
	}
}
