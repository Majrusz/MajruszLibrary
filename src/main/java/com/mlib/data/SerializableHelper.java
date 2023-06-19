package com.mlib.data;

import com.google.gson.JsonElement;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class SerializableHelper {
	public static < Type extends SerializableStructure > Type read( Supplier< Type > supplier, JsonElement element ) {
		Type instance = supplier.get();
		instance.read( element );

		return instance;
	}

	public static < Type extends SerializableStructure > Type read( Supplier< Type > supplier, FriendlyByteBuf buffer ) {
		Type instance = supplier.get();
		instance.read( buffer );

		return instance;
	}

	public static < Type extends SerializableStructure > Type read( Supplier< Type > supplier, CompoundTag tag ) {
		Type instance = supplier.get();
		instance.read( tag );

		return instance;
	}

	public static < Type extends SerializableStructure > void modify( Supplier< Type > supplier, CompoundTag tag, Consumer< Type > consumer ) {
		Type instance = supplier.get();
		instance.read( tag );
		consumer.accept( instance );
		instance.write( tag );
	}
}
