package com.mlib.data;

import com.google.gson.JsonElement;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

public interface ISerializable {
	void read( JsonElement element );

	void write( FriendlyByteBuf buffer );

	void read( FriendlyByteBuf buffer );

	void write( CompoundTag tag );

	void read( CompoundTag tag );
}
