package com.majruszlibrary.data;

import com.google.gson.JsonElement;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

public interface IReader< Type > {
	JsonElement writeJson( Type value );

	void writeBuffer( FriendlyByteBuf buffer, Type value );

	Tag writeTag( Type value );

	Type readJson( JsonElement json );

	Type readBuffer( FriendlyByteBuf buffer );

	Type readTag( Tag tag );
}
