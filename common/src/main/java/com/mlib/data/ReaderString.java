package com.mlib.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

class ReaderString implements IReader< String > {
	@Override
	public JsonElement writeJson( String value ) {
		return new JsonPrimitive( value );
	}

	@Override
	public String readJson( JsonElement json ) {
		return json.getAsString();
	}

	@Override
	public void writeBuffer( FriendlyByteBuf buffer, String value ) {
		buffer.writeUtf( value );
	}

	@Override
	public String readBuffer( FriendlyByteBuf buffer ) {
		return buffer.readUtf();
	}

	@Override
	public Tag writeTag( String value ) {
		return StringTag.valueOf( value );
	}

	@Override
	public String readTag( Tag tag ) {
		return tag.getAsString();
	}
}
