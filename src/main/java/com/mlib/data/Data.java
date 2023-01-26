package com.mlib.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.Function;

public abstract class Data< Type > implements ISerializable {
	String key = null;
	Type value = null;

	@Override
	public void read( JsonElement element ) {
		if( this.key != null ) {
			JsonObject object = element.getAsJsonObject();
			if( object.has( this.key ) ) {
				this.value = this.getJsonReader().read( object.get( this.key ) );
			}
		} else {
			this.value = this.getJsonReader().read( element );
		}
	}

	@Override
	public void write( FriendlyByteBuf buffer ) {
		this.getBufferWriter().write( buffer, this.value );
	}

	@Override
	public void read( FriendlyByteBuf buffer ) {
		this.value = this.getBufferReader().read( buffer );
	}

	@Override
	public void write( CompoundTag tag ) {
		if( this.key == null || this.value == null )
			return;

		this.getTagWriter().write( tag, this.key, this.value );
	}

	@Override
	public void read( CompoundTag tag ) {
		if( this.key == null || !tag.contains( this.key ) )
			return;

		this.value = this.getTagReader().read( tag, this.key );
	}

	public Type get() {
		return this.value;
	}

	public Data< Type > key( String key ) {
		this.key = key;

		return this;
	}

	public Data< Type > set( Type value ) {
		this.value = value;

		return this;
	}

	public Data< Type > set( Function< Type, Type > formula ) {
		this.value = formula.apply( this.value );

		return this;
	}

	public Data< Type > or( Type defaultValue ) {
		this.value = defaultValue;

		return this;
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
