package com.mlib.data;

import com.google.gson.JsonElement;
import com.mlib.annotation.Dist;
import com.mlib.annotation.OnlyIn;
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

	default void onServer( ServerPlayer player ) {}

	@OnlyIn( Dist.CLIENT )
	default void onClient() {}
}
