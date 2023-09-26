package net.mlib.data;

import com.google.gson.JsonElement;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

interface IReader< Type > {
	Type read( JsonElement element );

	void write( FriendlyByteBuf buffer, Type value );

	Type read( FriendlyByteBuf buffer );

	Tag write( Type value );

	Type read( Tag tag );
}
