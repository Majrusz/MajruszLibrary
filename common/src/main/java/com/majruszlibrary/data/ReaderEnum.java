package com.majruszlibrary.data;

import java.util.function.Supplier;

class ReaderEnum< Type extends Enum< ? > > extends ReaderStringCustom< Type > {
	final Supplier< Type[] > values;

	public ReaderEnum( Supplier< Type[] > values ) {
		this.values = values;
	}

	@Override
	public Type convert( String id ) {
		Type[] values = this.values.get();
		for( Type value : values ) {
			if( id.equalsIgnoreCase( value.name() ) ) {
				return value;
			}
		}

		return values[ 0 ];
	}

	@Override
	public String convert( Type value ) {
		return value.name();
	}
}
