package com.mlib.data;

import net.minecraft.nbt.CompoundTag;

class DataStructure< Type extends SerializableStructure > extends Data< Type > {
	final java.util.function.Supplier< Type > instanceProvider;

	public DataStructure( String key, Supplier< Type > getter, Consumer< Type > setter, java.util.function.Supplier< Type > instanceProvider ) {
		super( key, getter, setter );

		this.instanceProvider = instanceProvider;
	}

	@Override
	protected JsonReader< Type > getJsonReader() {
		return element->{
			Type structure = this.instanceProvider.get();
			structure.read( element );

			return structure;
		};
	}

	@Override
	protected BufferWriter< Type > getBufferWriter() {
		return ( buffer, value )->value.write( buffer );
	}

	@Override
	protected BufferReader< Type > getBufferReader() {
		return buffer->{
			Type structure = this.instanceProvider.get();
			structure.read( buffer );

			return structure;
		};
	}

	@Override
	protected TagWriter< Type > getTagWriter() {
		return ( tag, key, value )->{
			CompoundTag subtag = new CompoundTag();
			value.write( subtag );

			tag.put( key, subtag );
		};
	}

	@Override
	protected TagReader< Type > getTagReader() {
		return ( tag, key )->{
			Type structure = this.instanceProvider.get();
			structure.read( tag.getCompound( key ) );

			return structure;
		};
	}

	@FunctionalInterface
	public interface Supplier< Type extends SerializableStructure > extends java.util.function.Supplier< Type > {}

	@FunctionalInterface
	public interface Consumer< Type extends SerializableStructure > extends java.util.function.Consumer< Type > {}
}
