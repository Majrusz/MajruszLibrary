package com.majruszlibrary.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.NumericTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

class ReaderInteger implements IReader< Integer > {
	@Override
	public JsonElement writeJson( Integer value ) {
		return new JsonPrimitive( value );
	}

	@Override
	public void writeBuffer( FriendlyByteBuf buffer, Integer value ) {
		buffer.writeInt( value );
	}

	@Override
	public Tag writeTag( Integer value ) {
		return IntTag.valueOf( value );
	}

	@Override
	public Integer readJson( JsonElement json ) {
		return json.getAsInt();
	}

	@Override
	public Integer readBuffer( FriendlyByteBuf buffer ) {
		return buffer.readInt();
	}

	@Override
	public Integer readTag( Tag tag ) {
		return ( ( NumericTag )tag ).getAsInt();
	}
}
