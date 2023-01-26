package com.mlib.data;

import java.util.function.Supplier;

class DataStructure< Type extends SerializableStructure > extends Data< Type > {
	final Supplier< Type > supplier;

	public DataStructure( Supplier< Type > supplier ) {
		this.supplier = supplier;
	}

	@Override
	protected JsonReader< Type > getJsonReader() {
		return element->{
			Type structure = this.supplier.get();
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
			Type structure = this.supplier.get();
			structure.read( buffer );

			return structure;
		};
	}

	@Override
	protected TagWriter< Type > getTagWriter() {
		return ( tag, key, value )->value.write( tag.getCompound( key ) );
	}

	@Override
	protected TagReader< Type > getTagReader() {
		return ( tag, key )->{
			Type structure = this.supplier.get();
			structure.read( tag.getCompound( key ) );

			return structure;
		};
	}
}
