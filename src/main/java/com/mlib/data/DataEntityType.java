package com.mlib.data;

import com.mlib.Utility;
import net.minecraft.world.entity.EntityType;

class DataEntityType extends Data< EntityType< ? > > {
	public DataEntityType( String key, Supplier getter, Consumer setter ) {
		super( key, getter, setter );
	}

	@Override
	protected JsonReader< EntityType< ? > > getJsonReader() {
		return element->Utility.getEntityType( element.getAsString() );
	}

	@Override
	protected BufferWriter< EntityType< ? > > getBufferWriter() {
		return ( buffer, value )->buffer.writeUtf( Utility.getRegistryString( value ) );
	}

	@Override
	protected BufferReader< EntityType< ? > > getBufferReader() {
		return buffer->Utility.getEntityType( buffer.readUtf() );
	}

	@Override
	protected TagWriter< EntityType< ? > > getTagWriter() {
		return ( tag, key, value )->tag.putString( key, Utility.getRegistryString( value ) );
	}

	@Override
	protected TagReader< EntityType< ? > > getTagReader() {
		return ( tag, key )->Utility.getEntityType( tag.getString( key ) );
	}

	@FunctionalInterface
	public interface Supplier extends java.util.function.Supplier< EntityType< ? > > {}

	@FunctionalInterface
	public interface Consumer extends java.util.function.Consumer< EntityType< ? > > {}
}
