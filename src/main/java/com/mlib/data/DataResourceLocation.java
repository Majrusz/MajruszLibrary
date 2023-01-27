package com.mlib.data;

import net.minecraft.resources.ResourceLocation;

class DataResourceLocation extends Data< ResourceLocation > {
	public DataResourceLocation( String key, Supplier getter, Consumer setter ) {
		super( key, getter, setter );
	}

	@Override
	protected JsonReader< ResourceLocation > getJsonReader() {
		return element->new ResourceLocation( element.getAsString() );
	}

	@Override
	protected BufferWriter< ResourceLocation > getBufferWriter() {
		return ( buffer, value )->buffer.writeUtf( value.toString() );
	}

	@Override
	protected BufferReader< ResourceLocation > getBufferReader() {
		return buffer->new ResourceLocation( buffer.readUtf() );
	}

	@Override
	protected TagWriter< ResourceLocation > getTagWriter() {
		return ( tag, key, value )->tag.putString( key, value.toString() );
	}

	@Override
	protected TagReader< ResourceLocation > getTagReader() {
		return ( tag, key )->new ResourceLocation( tag.getString( key ) );
	}

	@FunctionalInterface
	public interface Supplier extends java.util.function.Supplier< ResourceLocation > {}

	@FunctionalInterface
	public interface Consumer extends java.util.function.Consumer< ResourceLocation > {}
}
