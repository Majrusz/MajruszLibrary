package com.majruszlibrary.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class SerializableObject< Type > implements ISerializable< Type > {
	protected final List< ISerializable< Type > > serializables = new ArrayList<>();
	protected final Class< Type > clazz;

	@Override
	public < JsonType extends JsonElement > JsonType writeJson( Type value, JsonType json ) {
		this.serializables.forEach( serializable->serializable.writeJson( value, json ) );

		return json;
	}

	@Override
	public FriendlyByteBuf writeBuffer( Type value, FriendlyByteBuf buffer ) {
		this.serializables.forEach( serializable->serializable.writeBuffer( value, buffer ) );

		return buffer;
	}

	@Override
	public < TagType extends Tag > TagType writeTag( Type value, TagType tag ) {
		this.serializables.forEach( serializable->serializable.writeTag( value, tag ) );

		return tag;
	}

	@Override
	public Type readJson( Type value, JsonElement json ) {
		this.serializables.forEach( serializable->{
			try {
				serializable.readJson( value, json );
			} catch( Exception exception ) {
			}
		} );

		return value;
	}

	@Override
	public Type readBuffer( Type value, FriendlyByteBuf buffer ) {
		this.serializables.forEach( serializable->{
			try {
				serializable.readBuffer( value, buffer );
			} catch( Exception exception ) {
			}
		} );

		return value;
	}

	@Override
	public Type readTag( Type value, Tag tag ) {
		this.serializables.forEach( serializable->{
			try {
				serializable.readTag( value, tag );
			} catch( Exception exception ) {
			}
		} );

		return value;
	}

	public < ValueType > SerializableObject< Type > define( String id, IReader< ValueType > reader, Function< Type, ValueType > getter,
		BiConsumer< Type, ValueType > setter
	) {
		this.serializables.add( new ReaderKey<>( id, reader, getter, setter ) );

		return this;
	}

	SerializableObject( Class< Type > clazz ) {
		this.clazz = clazz;
	}

	public SerializableObject< Type > define( String id, Consumer< SerializableObject< Type > > consumer ) {
		SerializableObject< Type > subobject = new SerializableObject<>( this.clazz );
		consumer.accept( subobject );

		this.serializables.add( new ISerializable<>() {
			@Override
			public < JsonType extends JsonElement > JsonType writeJson( Type value, JsonType json ) {
				JsonObject jsonObject = json.getAsJsonObject();
				JsonElement subjson = subobject.writeJson( value, new JsonObject() );
				if( subjson != null ) {
					jsonObject.add( id, subjson );
				}

				return json;
			}

			@Override
			public FriendlyByteBuf writeBuffer( Type value, FriendlyByteBuf buffer ) {
				subobject.writeBuffer( value, buffer );

				return buffer;
			}

			@Override
			public < TagType extends Tag > TagType writeTag( Type value, TagType tag ) {
				CompoundTag compoundTag = ( CompoundTag )tag;
				Tag subtag = subobject.writeTag( value, new CompoundTag() );
				if( subtag != null ) {
					compoundTag.put( id, subtag );
				}

				return tag;
			}

			@Override
			public Type readJson( Type value, JsonElement json ) {
				JsonObject jsonObject = json.getAsJsonObject();
				if( jsonObject.has( id ) ) {
					subobject.readJson( value, jsonObject.get( id ) );
				}

				return value;
			}

			@Override
			public Type readBuffer( Type value, FriendlyByteBuf buffer ) {
				subobject.readBuffer( value, buffer );

				return value;
			}

			@Override
			public Type readTag( Type value, Tag tag ) {
				CompoundTag compoundTag = ( CompoundTag )tag;
				if( compoundTag.contains( id ) ) {
					subobject.readTag( value, compoundTag.get( id ) );
				}

				return value;
			}
		} );

		return this;
	}
}
