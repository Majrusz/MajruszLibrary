package com.majruszlibrary.data;

import com.google.gson.JsonElement;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.BiConsumer;
import java.util.function.Function;

class ReaderKey< ObjectType, ValueType > {
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

	public JsonElement writeJson( ObjectType object ) {
		return this.reader.writeJson( this.getter.apply( object ) );
	}

	public void writeBuffer( FriendlyByteBuf buffer, ObjectType object ) {
		this.reader.writeBuffer( buffer, this.getter.apply( object ) );
	}

	public Tag writeTag( ObjectType object ) {
		return this.reader.writeTag( this.getter.apply( object ) );
	}

	public void readJson( ObjectType object, JsonElement json ) {
		this.setter.accept( object, this.reader.readJson( json ) );
	}

	public void readBuffer( ObjectType object, FriendlyByteBuf buffer ) {
		this.setter.accept( object, this.reader.readBuffer( buffer ) );
	}

	public void readTag( ObjectType object, Tag tag ) {
		this.setter.accept( object, this.reader.readTag( tag ) );
	}
}
