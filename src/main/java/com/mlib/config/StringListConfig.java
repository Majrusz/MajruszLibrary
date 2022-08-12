package com.mlib.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StringListConfig extends ValueConfig< List< ? extends String > > {
	public StringListConfig( String name, String comment, boolean worldRestartRequired, List< ? extends String > defaultValue ) {
		super( name, comment, worldRestartRequired, defaultValue );
	}

	public StringListConfig( String name, String comment, boolean worldRestartRequired, String... values ) {
		super( name, comment, worldRestartRequired, Arrays.asList( values ) );
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
