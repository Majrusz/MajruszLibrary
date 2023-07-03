package com.mlib.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

record DataMap< Type >( Supplier< Map< String, Type > > getter, Consumer< Map< String, Type > > setter, IReader< Type > reader ) implements ISerializable {
	@Override
	public void read( JsonElement element ) {
		JsonObject jsonObject = element.getAsJsonObject();
		Map< String, Type > values = new HashMap<>();
		for( String key : jsonObject.keySet() ) {
			values.put( key, this.reader.read( jsonObject.get( key ) ) );
		}

		this.setter.accept( values );
	}

	@Override
	public void write( FriendlyByteBuf buffer ) {
		Map< String, Type > values = this.getter.get();
		buffer.writeVarInt( values.size() );
		for( String key : values.keySet() ) {
			buffer.writeUtf( key );
			this.reader.write( buffer, values.get( key ) );
		}
	}

	@Override
	public void read( FriendlyByteBuf buffer ) {
		Map< String, Type > values = new HashMap<>();
		int size = buffer.readVarInt();
		for( int idx = 0; idx < size; ++idx ) {
			String key = buffer.readUtf();
			values.put( key, this.reader.read( buffer ) );
		}

		this.setter.accept( values );
	}

	@Override
	public void write( Tag tag ) {
		CompoundTag compoundTag = ( CompoundTag )tag;
		Map< String, Type > values = this.getter.get();
		for( String key : values.keySet() ) {
			compoundTag.put( key, this.reader.write( values.get( key ) ) );
		}
	}

	@Override
	public void read( Tag tag ) {
		CompoundTag compoundTag = ( CompoundTag )tag;
		Map< String, Type > values = new HashMap<>();
		for( String key : compoundTag.getAllKeys() ) {
			values.put( key, this.reader.read( compoundTag.get( key ) ) );
		}

		this.setter.accept( values );
	}
}
