package com.mlib.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class DoubleArrayConfig implements IConfigurable {
	static final Function< Integer, String > DEFAULT_FORMAT = idx->String.format( "%d", idx + 1 );
	final ConfigGroup group;
	final List< DoubleConfig > values = new ArrayList<>();

	public DoubleArrayConfig( String name, String comment, Function< Integer, String > format, boolean worldRestartRequired, double min, double max, double... defaultValues ) {
		this.group = new ConfigGroup( name, comment );
		for( int idx = 0; idx < defaultValues.length; ++idx ) {
			DoubleConfig config = new DoubleConfig( format.apply( idx ), "", worldRestartRequired, defaultValues[ idx ], min, max );
			this.values.add( config );
			this.group.addConfig( config );
		}
	}

	public DoubleArrayConfig( String name, String comment, boolean worldRestartRequired, double min, double max, double... defaultValues ) {
		this( name, comment, DEFAULT_FORMAT, worldRestartRequired, min, max, defaultValues );
	}

	public int asTicks( int idx ) {
		return getConfig( idx ).asTicks();
	}

	public float asFloat( int idx ) {
		return getConfig( idx ).asFloat();
	}

	public Double get( int idx ) {
		return getConfig( idx ).get();
	}

	public DoubleConfig getConfig( int idx ) {
		return this.values.get( Math.min( idx, this.values.size() - 1 ) );
	}

	@Override
	public String getName() {
		return this.group.getName();
	}

	@Override
	public String getComment() {
		return this.group.getComment();
	}

	@Override
	public void build( ForgeConfigSpec.Builder builder ) {
		this.group.build( builder );
	}
}
