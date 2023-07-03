package com.mlib.data;

import com.google.gson.JsonElement;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class SerializableHelper {
	public static < Type extends ISerializable > Type read( Supplier< Type > supplier, JsonElement element ) {
		Type instance = supplier.get();
		instance.read( element );

		return instance;
	}

	public static < Type extends ISerializable > Type read( Supplier< Type > supplier, FriendlyByteBuf buffer ) {
		Type instance = supplier.get();
		instance.read( buffer );

		return instance;
	}

	public static < Type extends ISerializable > Type read( Supplier< Type > supplier, Tag tag ) {
		Type instance = supplier.get();
		instance.read( tag );

		return instance;
	}

	public static < Type extends ISerializable, TagType extends Tag > TagType write( Supplier< Type > supplier, TagType tag ) {
		Type instance = supplier.get();
		instance.write( tag );

		return tag;
	}

	public static < Type extends ISerializable > void modify( Supplier< Type > supplier, Tag tag, Consumer< Type > consumer ) {
		Type instance = supplier.get();
		instance.read( tag );
		consumer.accept( instance );
		instance.write( tag );
	}
}
