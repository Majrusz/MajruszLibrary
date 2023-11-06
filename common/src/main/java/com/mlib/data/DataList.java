package com.mlib.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

record DataList< ObjectType, ValueType >(
	Getter< ObjectType, ValueType > getter, Setter< ObjectType, ValueType > setter, IReader< ValueType > reader, String key
) implements ISerializable< ObjectType > {
	@Override
	public void write( ObjectType object, JsonElement json ) {
		JsonArray jsonArray = new JsonArray();
		for( ValueType value : this.getter.apply( object ) ) {
			jsonArray.add( this.reader.writeJson( value ) );
		}
		json.getAsJsonObject().add( this.key, jsonArray );
	}

	@Override
	public void read( ObjectType object, JsonElement json ) {
		JsonObject jsonObject = json.getAsJsonObject();
		if( jsonObject.has( this.key ) ) {
			List< ValueType > values = new ArrayList<>();
			for( JsonElement subjson : jsonObject.getAsJsonArray( this.key ) ) {
				values.add( this.reader.readJson( subjson ) );
			}

			this.setter.accept( object, values );
		}
	}

	@Override
	public void write( ObjectType object, FriendlyByteBuf buffer ) {
		buffer.writeCollection( this.getter.apply( object ), this.reader::writeBuffer );
	}

	@Override
	public void read( ObjectType object, FriendlyByteBuf buffer ) {
		this.setter.accept( object, buffer.readCollection( ArrayList::new, this.reader::readBuffer ) );
	}

	@Override
	public void write( ObjectType object, Tag tag ) {
		ListTag listTag = new ListTag();
		for( ValueType value : this.getter.apply( object ) ) {
			listTag.add( this.reader.writeTag( value ) );
		}
		( ( CompoundTag )tag ).put( this.key, listTag );
	}

	@Override
	public void read( ObjectType object, Tag tag ) {
		CompoundTag compoundTag = ( CompoundTag )tag;
		if( compoundTag.contains( this.key ) ) {
			ListTag listTag = compoundTag.getList( this.key, 10 );
			List< ValueType > values = new ArrayList<>();
			for( Tag subtag : listTag ) {
				values.add( this.reader.readTag( subtag ) );
			}

			this.setter.accept( object, values );
		}
	}

	public interface Getter< ObjectType, ValueType > extends Function< ObjectType, List< ValueType > > {}

	public interface Setter< ObjectType, ValueType > extends BiConsumer< ObjectType, List< ValueType > > {}
}
