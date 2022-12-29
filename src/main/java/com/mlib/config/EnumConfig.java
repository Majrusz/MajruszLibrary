package com.mlib.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class EnumConfig< Type extends Enum< Type > > extends ValueConfig< Type > {
	public EnumConfig( Type defaultValue ) {
		super( defaultValue );
	}

	@Override
	public void build( ForgeConfigSpec.Builder builder ) {
		super.build( builder );

		this.config = builder.defineEnum( this.name, this.defaultValue );
	}
}
