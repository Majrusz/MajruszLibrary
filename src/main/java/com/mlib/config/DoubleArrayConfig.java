package com.mlib.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class DoubleArrayConfig extends UserConfig {
	static final Function< Integer, String > DEFAULT_FORMAT = idx->String.format( "%d", idx + 1 );
	final ConfigGroup group;
	final List< DoubleConfig > values = new ArrayList<>();

	public DoubleArrayConfig( String name, String comment, Function< Integer, String > format, boolean requiresWorldRestart, double min, double max, double... defaultValues ) {
		super( name, comment );

		this.group = new ConfigGroup( name, comment );
		for( int idx = 0; idx < defaultValues.length; ++idx ) {
			DoubleConfig config = new DoubleConfig( format.apply( idx ), "", requiresWorldRestart, defaultValues[ idx ], min, max );
			this.values.add( config );
			this.group.addConfig( config );
		}
	}

	public DoubleArrayConfig( String name, String comment, boolean requiresWorldRestart, double min, double max, double... defaultValues ) {
		this( name, comment, DEFAULT_FORMAT, requiresWorldRestart, min, max, defaultValues );
	}

	public int asTicks( int idx ) {
		return this.values.get( idx ).asTicks();
	}

	public float asFloat( int idx ) {
		return this.values.get( idx ).asFloat();
	}

	public Double get( int idx ) {
		return this.values.get( idx ).get();
	}

	@Override
	public void build( ForgeConfigSpec.Builder builder ) {
		this.group.build( builder );
	}
}
