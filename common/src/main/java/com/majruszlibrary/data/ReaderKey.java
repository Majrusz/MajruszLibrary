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
		ValueType value = this.getter.apply( object );
		if( value != null || this.reader instanceof ReaderClass ) {
			return this.reader.writeJson( value );
		}

		return null;
	}

	public void writeBuffer( FriendlyByteBuf buffer, ObjectType object ) {
		ValueType value = this.getter.apply( object );
		if( value != null || this.reader instanceof ReaderClass ) {
			this.reader.writeBuffer( buffer, this.getter.apply( object ) );
		}
	}

	public Tag writeTag( ObjectType object ) {
		ValueType value = this.getter.apply( object );
		if( value != null || this.reader instanceof ReaderClass ) {
			return this.reader.writeTag( value );
		}

		return null;
	}

	public void readJson( ObjectType object, JsonElement json ) {
		ValueType value = this.reader.readJson( json );
		if( value != null || this.reader instanceof ReaderClass ) {
			this.setter.accept( object, value );
		}
	}

	public void readBuffer( ObjectType object, FriendlyByteBuf buffer ) {
		ValueType value = this.reader.readBuffer( buffer );
		if( value != null || this.reader instanceof ReaderClass ) {
			this.setter.accept( object, value );
		}
	}

	public void readTag( ObjectType object, Tag tag ) {
		ValueType value = this.reader.readTag( tag );
		if( value != null || this.reader instanceof ReaderClass ) {
			this.setter.accept( object, value );
		}
	}
}
