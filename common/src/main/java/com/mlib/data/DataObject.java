package com.mlib.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.BiConsumer;
import java.util.function.Function;

record DataObject< ObjectType, ValueType >(
	Function< ObjectType, ValueType > getter, BiConsumer< ObjectType, ValueType > setter, IReader< ValueType > reader, String key
) implements ISerializable< ObjectType > {
	@Override
	public void write( ObjectType object, JsonElement json ) {
		if( this.getter.apply( object ) == null ) {
			return;
		}

		JsonObject jsonObject = json.getAsJsonObject();
		jsonObject.add( this.key, this.reader.writeJson( this.getter.apply( object ) ) );
	}

	@Override
	public void read( ObjectType object, JsonElement json ) {
		JsonObject jsonObject = json.getAsJsonObject();
		if( jsonObject.has( this.key ) ) {
			this.setter.accept( object, this.reader.readJson( jsonObject.get( this.key ) ) );
		}
	}

	@Override
	public void write( ObjectType object, FriendlyByteBuf buffer ) {
		this.reader.writeBuffer( buffer, this.getter.apply( object ) );
	}

	@Override
	public void read( ObjectType object, FriendlyByteBuf buffer ) {
		this.setter.accept( object, this.reader.readBuffer( buffer ) );
	}

	@Override
	public void write( ObjectType object, Tag tag ) {
		if( this.getter.apply( object ) == null ) {
			return;
		}

		CompoundTag compoundTag = ( CompoundTag )tag;
		compoundTag.put( this.key, this.reader.writeTag( this.getter.apply( object ) ) );
	}

	@Override
	public void read( ObjectType object, Tag tag ) {
		CompoundTag compoundTag = ( CompoundTag )tag;
		if( compoundTag.contains( this.key ) ) {
			this.setter.accept( object, this.reader.readTag( compoundTag.get( this.key ) ) );
		}
	}

	public interface Getter< ObjectType, ValueType > extends Function< ObjectType, ValueType > {}

	public interface Setter< ObjectType, ValueType > extends BiConsumer< ObjectType, ValueType > {}
}
