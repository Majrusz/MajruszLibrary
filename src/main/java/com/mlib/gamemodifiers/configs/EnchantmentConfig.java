package com.mlib.gamemodifiers.configs;

import com.mlib.config.BooleanConfig;
import com.mlib.config.ConfigGroup;

public class EnchantmentConfig extends ConfigGroup {
	final BooleanConfig availability;

	public EnchantmentConfig( String groupName ) {
		super( groupName, "" );
		this.availability = new BooleanConfig( "is_enabled", "Makes this enchantment obtainable in survival mode.", false, true );
		this.addConfigs( this.availability );
	}

	public boolean isEnabled() {
		return this.availability.isEnabled();
	}
}
