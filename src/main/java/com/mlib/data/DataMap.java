package com.mlib.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.HashMap;
import java.util.Map;

class DataMap< Type extends SerializableStructure > extends Data< Map< String, Type > > {
	final java.util.function.Supplier< Type > instanceProvider;

	public DataMap( String key, Supplier< Type > getter, Consumer< Type > setter, java.util.function.Supplier< Type > instanceProvider ) {
		super( key, getter, setter );

		this.instanceProvider = instanceProvider;
	}

	@Override
	protected JsonReader< Map< String, Type > > getJsonReader() {
		return element->{
			Map< String, Type > value = new HashMap<>();
			element.getAsJsonObject().keySet()
				.forEach( subkey->{
					Type subvalue = this.instanceProvider.get();
					subvalue.read( element.getAsJsonObject().get( subkey ) );

					value.put( subkey, subvalue );
				} );

			return value;
		};
	}

	@Override
	protected BufferWriter< Map< String, Type > > getBufferWriter() {
		return ( buffer, value )->buffer.writeMap( value, FriendlyByteBuf::writeUtf, ( subbuffer, subvalue )->subvalue.write( subbuffer ) );
	}

	@Override
	protected BufferReader< Map< String, Type > > getBufferReader() {
		return buffer->buffer.readMap( FriendlyByteBuf::readUtf, subbuffer->{
			Type subvalue = this.instanceProvider.get();
			subvalue.read( subbuffer );

			return subvalue;
		} );
	}

	@Override
	protected TagWriter< Map< String, Type > > getTagWriter() {
		return ( tag, key, value )->{
			CompoundTag subtag = new CompoundTag();
			value.forEach( ( subkey, subvalue )->{
				CompoundTag subsubtag = new CompoundTag();
				subvalue.write( subsubtag );

				subtag.put( subkey, subsubtag );
			} );

			tag.put( key, subtag );
		};
	}

	@Override
	protected TagReader< Map< String, Type > > getTagReader() {
		return ( tag, key )->{
			Map< String, Type > value = new HashMap<>();
			CompoundTag subtag = tag.getCompound( key );
			subtag.getAllKeys()
				.forEach( subkey->{
					Type subvalue = this.instanceProvider.get();
					subvalue.read( subtag.getCompound( subkey ) );

					value.put( subkey, subvalue );
				} );

			return value;
		};
	}

	@FunctionalInterface
	public interface Supplier< Type > extends java.util.function.Supplier< Map< String, Type > > {}

	@FunctionalInterface
	public interface Consumer< Type > extends java.util.function.Consumer< Map< String, Type > > {}
}
