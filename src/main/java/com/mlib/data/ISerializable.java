package com.mlib.data;

import com.google.gson.JsonElement;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

public interface ISerializable {
	void read( JsonElement element );

	void write( FriendlyByteBuf buffer );

	void read( FriendlyByteBuf buffer );

	void write( Tag tag );

	void read( Tag tag );

	default void onServer( ServerPlayer sender, NetworkEvent.Context context ) {}

	@OnlyIn( Dist.CLIENT )
	default void onClient( NetworkEvent.Context context ) {}
}
