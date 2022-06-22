package com.mlib.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StringListConfig extends ValueConfig< List< ? extends String >, ForgeConfigSpec.ConfigValue< List< ? extends String > > > {
	public StringListConfig( String name, String comment, boolean requiresWorldRestart, List< ? extends String > defaultValue ) {
		super( name, comment, requiresWorldRestart, defaultValue );
	}

	public StringListConfig( String name, String comment, boolean requiresWorldRestart, String... values ) {
		super( name, comment, requiresWorldRestart, toList( values ) );
	}

	@Override
	public ForgeConfigSpec.ConfigValue< List< ? extends String > > buildValue( ForgeConfigSpec.Builder builder ) {
		return builder.defineList( this.name, this.defaultValue, list->true );
	}

	public boolean contains( String value ) {
		return this.config.get().contains( value );
	}

	private static List< ? extends String > toList( String... values ) {
		List< String > list = new ArrayList<>();
		Collections.addAll( list, values );

		return list;
	}
}
