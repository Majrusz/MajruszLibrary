package com.mlib.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

class DataList< Type extends ISerializable > extends Data< List< Type > > {
	final Supplier< Type > supplier;

	public DataList( Supplier< Type > supplier ) {
		this.supplier = supplier;
	}

	@Override
	protected JsonReader< List< Type > > getJsonReader() {
		return element->{
			List< Type > value = new ArrayList<>();
			element.getAsJsonArray()
				.forEach( subelement->{
					Type subvalue = this.supplier.get();
					subvalue.read( subelement );

					value.add( subvalue );
				} );

			return value;
		};
	}

	@Override
	protected BufferWriter< List< Type > > getBufferWriter() {
		return ( buffer, value )->buffer.writeCollection( value, ( subbuffer, subvalue )->subvalue.write( subbuffer ) );
	}

	@Override
	protected BufferReader< List< Type > > getBufferReader() {
		return buffer->buffer.readList( subbuffer->{
			Type subvalue = this.supplier.get();
			subvalue.read( subbuffer );

			return subvalue;
		} );
	}

	@Override
	protected TagWriter< List< Type > > getTagWriter() {
		return ( tag, key, value )->{
			ListTag list = new ListTag();
			value.forEach( subvalue->{
				CompoundTag subtag = new CompoundTag();
				subvalue.write( subtag );

				list.add( subtag );
			} );

			tag.put( key, list );
		};
	}

	@Override
	protected TagReader< List< Type > > getTagReader() {
		return ( tag, key )->{
			List< Type > value = new ArrayList<>();
			tag.getList( key, 10 )
				.forEach( subtag->{
					Type subvalue = this.supplier.get();
					subvalue.read( ( CompoundTag )subtag );

					value.add( subvalue );
				} );

			return value;
		};
	}
}
