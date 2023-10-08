package com.mlib.data;

import com.google.gson.JsonElement;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

interface IReader< Type > {
	JsonElement writeJson( Type value );

	Type readJson( JsonElement element );

	void writeBuffer( FriendlyByteBuf buffer, Type value );

	Type readBuffer( FriendlyByteBuf buffer );

	Tag writeTag( Type value );

	Type readTag( Tag tag );
}
