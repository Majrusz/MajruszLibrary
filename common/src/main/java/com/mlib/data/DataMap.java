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

record DataMap< Type >( Getter< Type > getter, Setter< Type > setter, IReader< Type > reader ) implements ISerializable {
	@Override
	public void write( JsonElement element ) {
		JsonObject object = element.getAsJsonObject();
		Map< String, Type > values = this.getter.get();
		for( String key : values.keySet() ) {
			object.add( key, this.reader.writeJson( values.get( key ) ) );
		}
	}

	@Override
	public void read( JsonElement element ) {
		JsonObject object = element.getAsJsonObject();
		Map< String, Type > values = new HashMap<>();
		for( String key : object.keySet() ) {
			values.put( key, this.reader.readJson( object.get( key ) ) );
		}

		this.setter.accept( values );
	}

	@Override
	public void write( FriendlyByteBuf buffer ) {
		Map< String, Type > values = this.getter.get();
		buffer.writeVarInt( values.size() );
		for( String key : values.keySet() ) {
			buffer.writeUtf( key );
			this.reader.writeBuffer( buffer, values.get( key ) );
		}
	}

	@Override
	public void read( FriendlyByteBuf buffer ) {
		Map< String, Type > values = new HashMap<>();
		int size = buffer.readVarInt();
		for( int idx = 0; idx < size; ++idx ) {
			String key = buffer.readUtf();
			values.put( key, this.reader.readBuffer( buffer ) );
		}

		this.setter.accept( values );
	}

	@Override
	public void write( Tag tag ) {
		CompoundTag compoundTag = ( CompoundTag )tag;
		Map< String, Type > values = this.getter.get();
		for( String key : values.keySet() ) {
			compoundTag.put( key, this.reader.writeTag( values.get( key ) ) );
		}
	}

	@Override
	public void read( Tag tag ) {
		CompoundTag compoundTag = ( CompoundTag )tag;
		Map< String, Type > values = new HashMap<>();
		for( String key : compoundTag.getAllKeys() ) {
			values.put( key, this.reader.readTag( compoundTag.get( key ) ) );
		}

		this.setter.accept( values );
	}

	public interface Getter< Type > extends Supplier< Map< String, Type > > {}

	public interface Setter< Type > extends Consumer< Map< String, Type > > {}
}
