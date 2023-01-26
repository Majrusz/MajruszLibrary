package com.mlib.data;

import java.util.function.Supplier;

class DataEnum< Type extends Enum< ? > > extends Data< Type > {
	final Supplier< Type[] > supplier;

	public DataEnum( Supplier< Type[] > supplier ) {
		this.supplier = supplier;
	}

	@Override
	protected JsonReader< Type > getJsonReader() {
		return element->this.toEnum( element.getAsString() );
	}

	@Override
	protected BufferWriter< Type > getBufferWriter() {
		return ( buffer, value )->buffer.writeUtf( this.toString( value ) );
	}

	@Override
	protected BufferReader< Type > getBufferReader() {
		return buffer->this.toEnum( buffer.readUtf() );
	}

	@Override
	protected TagWriter< Type > getTagWriter() {
		return ( tag, key, value )->tag.putString( key, this.toString( value ) );
	}

	@Override
	protected TagReader< Type > getTagReader() {
		return ( tag, key )->this.toEnum( tag.getString( key ) );
	}

	private Type toEnum( String name ) {
		Type[] values = this.supplier.get();
		for( Type value : values ) {
			if( name.equalsIgnoreCase( value.toString() ) ) {
				return value;
			}
		}

		return values[ 0 ];
	}

	private String toString( Type value ) {
		return value.toString();
	}
}
