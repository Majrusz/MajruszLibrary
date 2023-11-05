package com.mlib.data;

import com.google.gson.JsonElement;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.Map;
import java.util.function.Consumer;

public final class Serializables {
	private static final Map< Class< ? >, Serializable< ? > > SERIALIZABLES = new Object2ObjectOpenHashMap<>();

	public static < Type > Serializable< Type > get( Class< Type > clazz ) {
		synchronized( SERIALIZABLES ) {
			return ( Serializable< Type > )SERIALIZABLES.computeIfAbsent( clazz, subclazz->new Serializable< Type >() );
		}
	}

	public static < Type > Type read( Type object, JsonElement json ) {
		Serializables.getUnsafe( object.getClass() ).read( object, json );

		return object;
	}

	public static < Type > Type read( Type object, FriendlyByteBuf buffer ) {
		Serializables.getUnsafe( object.getClass() ).read( object, buffer );

		return object;
	}

	public static < Type > Type read( Type object, Tag tag ) {
		Serializables.getUnsafe( object.getClass() ).read( object, tag );

		return object;
	}

	public static < Type > JsonElement write( Type object, JsonElement json ) {
		Serializables.getUnsafe( object.getClass() ).write( object, json );

		return json;
	}

	public static < Type > FriendlyByteBuf write( Type object, FriendlyByteBuf buffer ) {
		Serializables.getUnsafe( object.getClass() ).write( object, buffer );

		return buffer;
	}

	public static < Type, TagType extends Tag > TagType write( Type object, TagType tag ) {
		Serializables.getUnsafe( object.getClass() ).write( object, tag );

		return tag;
	}

	public static < Type > void modify( Type object, Tag tag, Consumer< Type > consumer ) {
		Serializable< Type > serializable = Serializables.getUnsafe( object.getClass() );

		serializable.read( object, tag );
		consumer.accept( object );
		serializable.write( object, tag );
	}

	private static < Type > Serializable< Type > getUnsafe( Class< ? > clazz ) {
		return ( Serializable< Type > )SERIALIZABLES.get( clazz );
	}

	private Serializables() {}
}
