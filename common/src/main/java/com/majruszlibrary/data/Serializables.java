package com.majruszlibrary.data;

import com.google.gson.JsonElement;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.Map;
import java.util.function.Consumer;

public final class Serializables {
	private static final Map< Class< ? >, ISerializable< ? > > SERIALIZABLES = new Object2ObjectOpenHashMap<>();

	public static < Type > SerializableObject< Type > get( Type value ) {
		return Serializables.get( ( Class< Type > )value.getClass() );
	}

	public static < Type > SerializableObject< Type > get( Class< Type > clazz ) {
		synchronized( SERIALIZABLES ) {
			return ( SerializableObject< Type > )SERIALIZABLES.computeIfAbsent( clazz, SerializableObject::new );
		}
	}

	public static < Type > SerializableClass< Type > getStatic( Class< Type > clazz ) {
		synchronized( SERIALIZABLES ) {
			return ( SerializableClass< Type > )SERIALIZABLES.computeIfAbsent( clazz, SerializableClass::new );
		}
	}

	public static < Type > JsonElement write( Type object, JsonElement json ) {
		return Serializables.getUnsafe( object ).writeJson( object, json );
	}

	public static JsonElement write( Class< ? > clazz, JsonElement json ) {
		return Serializables.getUnsafeStatic( clazz ).writeJson( null, json );
	}

	public static < Type > FriendlyByteBuf write( Type object, FriendlyByteBuf buffer ) {
		return Serializables.getUnsafe( object ).writeBuffer( object, buffer );
	}

	public static FriendlyByteBuf write( Class< ? > clazz, FriendlyByteBuf buffer ) {
		return Serializables.getUnsafeStatic( clazz ).writeBuffer( null, buffer );
	}

	public static < Type, TagType extends Tag > TagType write( Type object, TagType tag ) {
		return Serializables.getUnsafe( object ).writeTag( object, tag );
	}

	public static < TagType extends Tag > TagType write( Class< ? > clazz, TagType tag ) {
		return Serializables.getUnsafeStatic( clazz ).writeTag( null, tag );
	}

	public static < Type > Type read( Type object, JsonElement json ) {
		return Serializables.getUnsafe( object ).readJson( object, json );
	}

	public static void read( Class< ? > clazz, JsonElement json ) {
		Serializables.getUnsafeStatic( clazz ).readJson( null, json );
	}

	public static < Type > Type read( Type object, FriendlyByteBuf buffer ) {
		return Serializables.getUnsafe( object ).readBuffer( object, buffer );
	}

	public static void read( Class< ? > clazz, FriendlyByteBuf buffer ) {
		Serializables.getUnsafeStatic( clazz ).readBuffer( null, buffer );
	}

	public static < Type > Type read( Type object, Tag tag ) {
		return Serializables.getUnsafe( object ).readTag( object, tag );
	}

	public static void read( Class< ? > clazz, Tag tag ) {
		Serializables.getUnsafeStatic( clazz ).readTag( null, tag );
	}

	public static < Type > void modify( Type object, Tag tag, Consumer< Type > consumer ) {
		SerializableObject< Type > serializable = Serializables.getUnsafe( object );
		serializable.readTag( object, tag );
		consumer.accept( object );
		serializable.writeTag( object, tag );
	}

	private static < Type > SerializableObject< Type > getUnsafe( Type value ) {
		return Serializables.getUnsafe( ( Class< Type > )value.getClass() );
	}

	private static < Type > SerializableObject< Type > getUnsafe( Class< Type > clazz ) {
		return ( SerializableObject< Type > )SERIALIZABLES.get( clazz );
	}

	private static < Type > SerializableClass< Type > getUnsafeStatic( Class< Type > clazz ) {
		synchronized( SERIALIZABLES ) {
			return ( SerializableClass< Type > )SERIALIZABLES.get( clazz );
		}
	}

	private Serializables() {}
}
