package com.mlib.config;

import net.minecraft.network.chat.*;
import net.minecraftforge.common.ForgeConfigSpec;

public class StringConfig extends ValueConfig< String > {
	public StringConfig( String defaultValue ) {
		super( defaultValue );
	}

	@Override
	public void build( ForgeConfigSpec.Builder builder ) {
		super.build( builder );

		this.config = builder.define( this.name, this.defaultValue );
	}

	public Component asLiteral() {
		return new TextComponent( this.getOrDefault() );
	}

	public Component asTranslatable( Object... params ) {
		return new TranslatableComponent( this.getOrDefault(), params );
	}
}
