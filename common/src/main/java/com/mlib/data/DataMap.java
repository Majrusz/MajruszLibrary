package com.mlib.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import java.util.function.BiConsumer;
import java.util.function.Function;

record DataMap< ObjectType, ValueType >(
	Getter< ObjectType, ValueType > getter, Setter< ObjectType, ValueType > setter, IReader< ValueType > reader, String key
) implements ISerializable< ObjectType > {
	@Override
	public void write( ObjectType object, JsonElement json ) {
		JsonObject jsonObject = new JsonObject();
		Map< String, ValueType > values = this.getter.apply( object );
		for( String key : new TreeSet<>( values.keySet() ) ) {
			jsonObject.add( key, this.reader.writeJson( values.get( key ) ) );
		}
		json.getAsJsonObject().add( this.key, jsonObject );
	}

	@Override
	public void read( ObjectType object, JsonElement json ) {
		JsonObject jsonObject = json.getAsJsonObject();
		if( jsonObject.has( this.key ) ) {
			JsonObject subjsonObject = jsonObject.getAsJsonObject( this.key );
			Map< String, ValueType > values = new HashMap<>();
			for( String key : subjsonObject.keySet() ) {
				values.put( key, this.reader.readJson( subjsonObject.get( key ) ) );
			}

			this.setter.accept( object, values );
		}
	}

	@Override
	public void write( ObjectType object, FriendlyByteBuf buffer ) {
		Map< String, ValueType > values = this.getter.apply( object );
		buffer.writeVarInt( values.size() );
		for( String key : new TreeSet<>( values.keySet() ) ) {
			buffer.writeUtf( key );
			this.reader.writeBuffer( buffer, values.get( key ) );
		}
	}

	@Override
	public void read( ObjectType object, FriendlyByteBuf buffer ) {
		Map< String, ValueType > values = new HashMap<>();
		int size = buffer.readVarInt();
		for( int idx = 0; idx < size; ++idx ) {
			String key = buffer.readUtf();
			values.put( key, this.reader.readBuffer( buffer ) );
		}

		this.setter.accept( object, values );
	}

	@Override
	public void write( ObjectType object, Tag tag ) {
		CompoundTag compoundTag = new CompoundTag();
		Map< String, ValueType > values = this.getter.apply( object );
		for( String key : new TreeSet<>( values.keySet() ) ) {
			compoundTag.put( key, this.reader.writeTag( values.get( key ) ) );
		}
		( ( CompoundTag )tag ).put( this.key, compoundTag );
	}

	@Override
	public void read( ObjectType object, Tag tag ) {
		CompoundTag compoundTag = ( CompoundTag )tag;
		if( compoundTag.contains( this.key ) ) {
			CompoundTag subcompoundTag = new CompoundTag();
			Map< String, ValueType > values = new HashMap<>();
			for( String key : subcompoundTag.getAllKeys() ) {
				values.put( key, this.reader.readTag( subcompoundTag.get( key ) ) );
			}

			this.setter.accept( object, values );
		}
	}

	public interface Getter< ObjectType, ValueType > extends Function< ObjectType, Map< String, ValueType > > {}

	public interface Setter< ObjectType, ValueType > extends BiConsumer< ObjectType, Map< String, ValueType > > {}
}
