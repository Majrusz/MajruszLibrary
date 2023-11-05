package com.mlib.data;

import com.google.gson.JsonElement;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

public interface ISerializable< Type > {
	void write( Type object, JsonElement json );

	void read( Type object, JsonElement json );

	void write( Type object, FriendlyByteBuf buffer );

	void read( Type object, FriendlyByteBuf buffer );

	void write( Type object, Tag tag );

	void read( Type object, Tag tag );
}
