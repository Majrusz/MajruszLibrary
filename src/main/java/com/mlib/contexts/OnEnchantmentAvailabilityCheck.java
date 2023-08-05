package com.mlib.contexts;

import com.mlib.config.BooleanConfig;
import com.mlib.enchantments.CustomEnchantment;
import com.mlib.contexts.base.Condition;
import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;
import com.mlib.contexts.base.Priority;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class OnEnchantmentAvailabilityCheck {
	public static final Consumer< Data > ENABLE = data->data.isAvailable = true;

	public static Context< Data > listen( Consumer< Data > consumer ) {
		return Contexts.get( Data.class ).add( consumer );
	}

	public static Data dispatch( CustomEnchantment enchantment ) {
		return Contexts.get( Data.class ).dispatch( new Data( enchantment ) );
	}

	public static < Type extends CustomEnchantment > Condition< Data > is( Supplier< Type > enchantment ) {
		return new Condition< Data >( data->data.enchantment.equals( enchantment.get() ) )
			.priority( Priority.HIGH );
	}

	public static < DataType extends Data > Condition< DataType > excludable( boolean defaultValue ) {
		BooleanConfig availability = DefaultConfigs.excludable( defaultValue );

		return new Condition< DataType >( data->availability.getOrDefault() )
			.priority( Priority.HIGHEST )
			.configurable( true )
			.addConfig( availability );
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

	public static class DefaultConfigs {
		public static BooleanConfig excludable( boolean defaultValue ) {
			BooleanConfig availability = new BooleanConfig( defaultValue );
			availability.name( "is_enabled" ).comment( "Specifies whether this enchantment is obtainable in survival mode." );

			return availability;
		}
	}
}
