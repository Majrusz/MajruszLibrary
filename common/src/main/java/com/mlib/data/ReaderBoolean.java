package com.mlib.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

class ReaderBoolean implements IReader< Boolean > {
	@Override
	public JsonElement writeJson( Boolean value ) {
		return new JsonPrimitive( value );
	}

	@Override
	public void writeBuffer( FriendlyByteBuf buffer, Boolean value ) {
		buffer.writeBoolean( value );
	}

	@Override
	public Tag writeTag( Boolean value ) {
		return ByteTag.valueOf( value );
	}

	@Override
	public Boolean readJson( JsonElement json ) {
		return json.getAsBoolean();
	}

	@Override
	public Boolean readBuffer( FriendlyByteBuf buffer ) {
		return buffer.readBoolean();
	}

	@Override
	public Boolean readTag( Tag tag ) {
		return ( ( ByteTag )tag ).getAsByte() != 0;
	}
}
