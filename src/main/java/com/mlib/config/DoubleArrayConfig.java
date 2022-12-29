package com.mlib.config;

import com.mlib.math.Range;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class DoubleArrayConfig extends ConfigGroup {
	static final Function< Integer, String > DEFAULT_FORMAT = idx->String.format( "%d", idx + 1 );
	final List< DoubleConfig > values = new ArrayList<>();

	public DoubleArrayConfig( Function< Integer, String > format, Range< Double > range, double... defaultValues ) {
		for( int idx = 0; idx < defaultValues.length; ++idx ) {
			DoubleConfig config = new DoubleConfig( defaultValues[ idx ], range );
			this.values.add( config );
			this.addConfig( config.name( format.apply( idx ) ) );
		}
	}

	public DoubleArrayConfig( Range< Double > range, double... defaultValues ) {
		this( DEFAULT_FORMAT, range, defaultValues );
	}

	public int asTicks( int idx ) {
		return this.getConfig( idx ).asTicks();
	}

	public float asFloat( int idx ) {
		return this.getConfig( idx ).asFloat();
	}

	public Double get( int idx ) {
		return this.getConfig( idx ).get();
	}

	public Double getOrDefault( int idx ) {
		return this.getConfig( idx ).getOrDefault();
	}

	private DoubleConfig getConfig( int idx ) {
		return this.values.get( Math.min( idx, this.values.size() - 1 ) );
	}
}
