package com.mlib.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Arrays;
import java.util.List;

public class StringListConfig extends ValueConfig< List< ? extends String > > {
	public StringListConfig( List< ? extends String > defaultValue ) {
		super( defaultValue );
	}

	public StringListConfig( String... values ) {
		super( Arrays.asList( values ) );
	}

	@Override
	public void build( ForgeConfigSpec.Builder builder ) {
		super.build( builder );

		this.config = builder.defineList( this.name, this.defaultValue, list->true );
	}

	public boolean contains( String value ) {
		return this.config.get().contains( value );
	}
}
