package com.majruszlibrary.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.UUID;

class ReaderUUID implements IReader< UUID > {
	@Override
	public JsonElement writeJson( UUID value ) {
		return new JsonPrimitive( value.toString() );
	}

	@Override
	public UUID readJson( JsonElement json ) {
		return UUID.fromString( json.getAsString() );
	}

	@Override
	public void writeBuffer( FriendlyByteBuf buffer, UUID value ) {
		buffer.writeLong( value.getMostSignificantBits() );
		buffer.writeLong( value.getLeastSignificantBits() );
	}

	@Override
	public UUID readBuffer( FriendlyByteBuf buffer ) {
		return new UUID( buffer.readLong(), buffer.readLong() );
	}

	@Override
	public Tag writeTag( UUID value ) {
		return NbtUtils.createUUID( value );
	}

	@Override
	public UUID readTag( Tag tag ) {
		return NbtUtils.loadUUID( tag );
	}
}
