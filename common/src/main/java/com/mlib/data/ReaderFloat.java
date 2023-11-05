package com.mlib.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.NumericTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

class ReaderFloat implements IReader< Float > {
	@Override
	public JsonElement writeJson( Float value ) {
		return new JsonPrimitive( value );
	}

	@Override
	public Float readJson( JsonElement json ) {
		return json.getAsFloat();
	}

	@Override
	public void writeBuffer( FriendlyByteBuf buffer, Float value ) {
		buffer.writeFloat( value );
	}

	@Override
	public Float readBuffer( FriendlyByteBuf buffer ) {
		return buffer.readFloat();
	}

	@Override
	public Tag writeTag( Float value ) {
		return FloatTag.valueOf( value );
	}

	@Override
	public Float readTag( Tag tag ) {
		return ( ( NumericTag )tag ).getAsFloat();
	}
}
