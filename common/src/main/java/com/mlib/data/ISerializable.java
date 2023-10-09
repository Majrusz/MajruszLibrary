package com.mlib.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public interface ISerializable {
	void write( JsonElement element );

	void read( JsonElement element );

	void write( FriendlyByteBuf buffer );

	void read( FriendlyByteBuf buffer );

	void write( Tag tag );

	void read( Tag tag );

	default JsonElement getDefaultJson() {
		return new JsonObject();
	}

	default Tag getDefaultTag() {
		return new CompoundTag();
	}

	default void onServer( ServerPlayer player ) {}

	default void onClient() {}
}
