package com.mlib.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.Consumer;
import java.util.function.Supplier;

abstract class Data< Type > implements ISerializable {
	final String key;
	final Supplier< Type > getter;
	final Consumer< Type > setter;

	protected Data( String key, Supplier< Type > getter, Consumer< Type > setter ) {
		this.key = key;
		this.getter = getter;
		this.setter = setter;
	}

	@Override
	public void read( JsonElement element ) {
		if( this.key != null ) {
			JsonObject object = element.getAsJsonObject();
			if( object.has( this.key ) ) {
				this.set( this.getJsonReader().read( object.get( this.key ) ) );
			}
		} else {
			this.set( this.getJsonReader().read( element ) );
		}
	}

	@Override
	public void write( FriendlyByteBuf buffer ) {
		this.getBufferWriter().write( buffer, this.get() );
	}

	@Override
	public void read( FriendlyByteBuf buffer ) {
		this.set( this.getBufferReader().read( buffer ) );
	}

	@Override
	public void write( CompoundTag tag ) {
		if( this.key == null || this.get() == null )
			return;

		this.getTagWriter().write( tag, this.key, this.get() );
	}

	@Override
	public void read( CompoundTag tag ) {
		if( this.key == null || !tag.contains( this.key ) )
			return;

		this.set( this.getTagReader().read( tag, this.key ) );
	}

	public void set( Type value ) {
		this.setter.accept( value );
	}

	public Type get() {
		return this.getter.get();
	}

	protected abstract JsonReader< Type > getJsonReader();

	protected abstract BufferWriter< Type > getBufferWriter();

	protected abstract BufferReader< Type > getBufferReader();

	protected abstract TagWriter< Type > getTagWriter();

	protected abstract TagReader< Type > getTagReader();

	@FunctionalInterface
	protected interface JsonReader< Type > {
		Type read( JsonElement element );
	}

	@FunctionalInterface
	protected interface BufferWriter< Type > {
		void write( FriendlyByteBuf buffer, Type value );
	}

	@FunctionalInterface
	protected interface BufferReader< Type > {
		Type read( FriendlyByteBuf buffer );
	}

	@FunctionalInterface
	protected interface TagWriter< Type > {
		void write( CompoundTag tag, String key, Type value );
	}

	@FunctionalInterface
	protected interface TagReader< Type > {
		Type read( CompoundTag tag, String key );
	}
}
