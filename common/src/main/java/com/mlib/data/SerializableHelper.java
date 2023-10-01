package com.mlib.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
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

	public static < Type extends ISerializable > FriendlyByteBuf write( Supplier< Type > supplier, FriendlyByteBuf buffer ) {
		Type instance = supplier.get();
		instance.write( buffer );

		return buffer;
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

	static JsonElement getReadSubelement( JsonElement element, String key ) {
		if( key == null ) {
			return element;
		} else {
			JsonObject jsonObject = element.getAsJsonObject();

			return jsonObject.has( key ) ? jsonObject.get( key ) : null;
		}
	}

	static Tag getWriteSubtag( Tag tag, String key, Supplier< Tag > newSubtag ) {
		if( key == null ) {
			return tag;
		} else {
			Tag subtag = newSubtag.get();
			( ( CompoundTag )tag ).put( key, subtag );

			return subtag;
		}
	}

	static Tag getReadSubtag( Tag tag, String key ) {
		if( key == null ) {
			return tag;
		} else {
			CompoundTag subtag = ( CompoundTag )tag;

			return subtag.contains( key ) ? subtag.get( key ) : null;
		}
	}
}
