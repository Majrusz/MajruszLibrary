package com.mlib.data;

class DataEnum< Type extends Enum< ? > > extends Data< Type > {
	final java.util.function.Supplier< Type[] > values;

	public DataEnum( String key, Supplier< Type > getter, Consumer< Type > setter, java.util.function.Supplier< Type[] > values ) {
		super( key, getter, setter );

		this.values = values;
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
		Type[] values = this.values.get();
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

	@FunctionalInterface
	public interface Supplier< Type extends Enum< ? > > extends java.util.function.Supplier< Type > {}

	@FunctionalInterface
	public interface Consumer< Type extends Enum< ? > > extends java.util.function.Consumer< Type > {}
}
