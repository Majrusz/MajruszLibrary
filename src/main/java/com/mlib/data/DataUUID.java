package com.mlib.data;

import java.util.UUID;

class DataUUID extends Data< UUID > {
	public DataUUID( String key, Supplier getter, Consumer setter ) {
		super( key, getter, setter );
	}

	@Override
	protected JsonReader< UUID > getJsonReader() {
		return element->UUID.fromString( element.getAsString() );
	}

	@Override
	protected BufferWriter< UUID > getBufferWriter() {
		return ( buffer, value )->buffer.writeUtf( value.toString() );
	}

	@Override
	protected BufferReader< UUID > getBufferReader() {
		return buffer->UUID.fromString( buffer.readUtf() );
	}

	@Override
	protected TagWriter< UUID > getTagWriter() {
		return ( tag, key, value )->tag.putString( key, value.toString() );
	}

	@Override
	protected TagReader< UUID > getTagReader() {
		return ( tag, key )->UUID.fromString( tag.getString( key ) );
	}

	@FunctionalInterface
	public interface Supplier extends java.util.function.Supplier< UUID > {}

	@FunctionalInterface
	public interface Consumer extends java.util.function.Consumer< UUID > {}
}
