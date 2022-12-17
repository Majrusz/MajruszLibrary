package com.mlib.config;

import net.minecraft.network.chat.Component;
import net.minecraftforge.common.ForgeConfigSpec;

public class StringConfig extends ValueConfig< String > {
	public StringConfig( String name, String comment, boolean worldRestartRequired, String defaultValue ) {
		super( name, comment, worldRestartRequired, defaultValue );
	}

	@Override
	public void build( ForgeConfigSpec.Builder builder ) {
		super.build( builder );

		this.config = builder.define( this.name, this.defaultValue );
	}

	public Component asLiteral() {
		return Component.literal( this.getOrDefault() );
	}

	public Component asTranslatable() {
		return Component.translatable( this.getOrDefault() );
	}
}
