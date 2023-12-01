package com.majruszlibrary.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.BiConsumer;
import java.util.function.Function;

class ReaderKey< ObjectType, ValueType > implements ISerializable< ObjectType > {
	final String id;
	final IReader< ValueType > reader;
	final Function< ObjectType, ValueType > getter;
	final BiConsumer< ObjectType, ValueType > setter;

	public ReaderKey( String id, IReader< ValueType > reader, Function< ObjectType, ValueType > getter, BiConsumer< ObjectType, ValueType > setter ) {
		this.id = id;
		this.reader = reader;
		this.getter = getter;
		this.setter = setter;
	}

	@Override
	public < JsonType extends JsonElement > JsonType writeJson( ObjectType object, JsonType json ) {
		JsonObject jsonObject = json.getAsJsonObject();
		JsonElement subjson = this.reader.writeJson( this.getter.apply( object ) );
		if( subjson != null ) {
			jsonObject.add( this.id, subjson );
		}

		return json;
	}

	@Override
	public FriendlyByteBuf writeBuffer( ObjectType object, FriendlyByteBuf buffer ) {
		this.reader.writeBuffer( buffer, this.getter.apply( object ) );

		return buffer;
	}

	@Override
	public < TagType extends Tag > TagType writeTag( ObjectType object, TagType tag ) {
		CompoundTag compoundTag = ( CompoundTag )tag;
		Tag subtag = this.reader.writeTag( this.getter.apply( object ) );
		if( subtag != null ) {
			compoundTag.put( this.id, subtag );
		}

		return tag;
	}

	@Override
	public ObjectType readJson( ObjectType object, JsonElement json ) {
		JsonObject jsonObject = json.getAsJsonObject();
		if( jsonObject.has( this.id ) ) {
			this.setter.accept( object, this.reader.readJson( jsonObject.get( this.id ) ) );
		}

		return object;
	}

	@Override
	public ObjectType readBuffer( ObjectType object, FriendlyByteBuf buffer ) {
		this.setter.accept( object, this.reader.readBuffer( buffer ) );

		return object;
	}

	@Override
	public ObjectType readTag( ObjectType object, Tag tag ) {
		CompoundTag compoundTag = ( CompoundTag )tag;
		if( compoundTag.contains( this.id ) ) {
			this.setter.accept( object, this.reader.readTag( compoundTag.get( this.id ) ) );
		}

		return object;
	}
}
