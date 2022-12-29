package com.mlib.gamemodifiers.configs;

import com.mlib.config.BooleanConfig;
import com.mlib.config.ConfigGroup;

public class EnchantmentConfig extends ConfigGroup {
	final BooleanConfig availability = new BooleanConfig( true );

	public EnchantmentConfig() {
		this.addConfig( this.availability.name( "is_enabled" ).comment( "Makes this enchantment obtainable in survival mode." ) );
	}

	public boolean isEnabled() {
		return this.availability.isEnabled();
	}
}
