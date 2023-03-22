package com.mlib.gamemodifiers.contexts;

import com.mlib.config.BooleanConfig;
import com.mlib.enchantments.CustomEnchantment;
import com.mlib.gamemodifiers.Condition;
import com.mlib.gamemodifiers.Context;
import com.mlib.gamemodifiers.Contexts;
import com.mlib.gamemodifiers.Priority;

import java.util.function.Consumer;

public class OnEnchantmentAvailabilityCheck {
	public static final Consumer< Data > ENABLE = data->data.isAvailable = true;

	public static Context< Data > listen( Consumer< Data > consumer ) {
		return Contexts.get( Data.class ).add( consumer );
	}

	public static Data dispatch( CustomEnchantment enchantment ) {
		return Contexts.get( Data.class ).dispatch( new Data( enchantment ) );
	}

	public static < Type extends CustomEnchantment > Condition< Data > is( Class< Type > clazz ) {
		return new Condition< Data >( data->clazz.isInstance( data.enchantment ) )
			.priority( Priority.HIGH );
	}

	public static < DataType extends Data > Condition< DataType > excludable( boolean defaultValue ) {
		BooleanConfig availability = new BooleanConfig( defaultValue );

		return new Condition< DataType >( data->availability.getOrDefault() )
			.priority( Priority.HIGHEST )
			.configurable( true )
			.addConfig( availability.name( "is_enabled" ).comment( "Specifies whether this enchantment is obtainable in survival mode." ) );
	}

	public static < DataType extends Data > Condition< DataType > excludable() {
		return excludable( true );
	}

	public static class Data {
		public final CustomEnchantment enchantment;
		public boolean isAvailable = false;

		public Data( CustomEnchantment enchantment ) {
			this.enchantment = enchantment;
		}

		public boolean isEnabled() {
			return this.isAvailable;
		}
	}
}
