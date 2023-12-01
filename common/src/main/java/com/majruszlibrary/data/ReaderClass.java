package com.majruszlibrary.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

class ReaderClass implements IReader< Void > {
	final Class< ? > clazz;

	public ReaderClass( Class< ? > clazz ) {
		this.clazz = clazz;
	}

	@Override
	public JsonElement writeJson( Void value ) {
		return Serializables.write( this.clazz, new JsonObject() );
	}

	@Override
	public void writeBuffer( FriendlyByteBuf buffer, Void value ) {
		Serializables.write( this.clazz, buffer );
	}

	@Override
	public Tag writeTag( Void value ) {
		return Serializables.write( this.clazz, new CompoundTag() );
	}

	@Override
	public Void readJson( JsonElement json ) {
		Serializables.read( this.clazz, json );

		return null;
	}

	@Override
	public Void readBuffer( FriendlyByteBuf buffer ) {
		Serializables.read( this.clazz, buffer );

		return null;
	}

	@Override
	public Void readTag( Tag tag ) {
		Serializables.read( this.clazz, tag );

		return null;
	}
}
