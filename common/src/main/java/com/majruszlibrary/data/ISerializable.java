package com.majruszlibrary.data;

import com.google.gson.JsonElement;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

public interface ISerializable< Type > {
	< JsonType extends JsonElement > JsonType writeJson( Type value, JsonType json );

	FriendlyByteBuf writeBuffer( Type value, FriendlyByteBuf buffer );

	< TagType extends Tag > TagType writeTag( Type value, TagType tag );

	Type readJson( Type value, JsonElement json );

	Type readBuffer( Type value, FriendlyByteBuf buffer );

	Type readTag( Type value, Tag tag );
}
