package com.mlib.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.List;

/** Class representing list of strings config. */
public class StringListConfig extends BaseConfig {
	public ForgeConfigSpec.ConfigValue< List< ? extends String > > listValues;
	private final List< ? extends String > defaultValues;

	public StringListConfig( String name, String comment, boolean requiresWorldRestart, List< ? extends String > defaultValues ) {
		super( name, comment, requiresWorldRestart );
		this.defaultValues = defaultValues;
	}

	/** Checks if a list contains given value. */
	public boolean hasValue( String value ) {
		List< ? extends String > values = this.listValues.get();

		return values.contains( value );
	}

	/** Builds current config. */
	@Override
	public void build( ForgeConfigSpec.Builder builder ) {
		super.build( builder );

		this.listValues = builder.defineList( this.name, this.defaultValues, x->true );
	}
}
