package com.majruszlibrary.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class SerializableObject< Type > implements ISerializable< Type > {
	protected final List< ReaderKey< Type, ? > > readers = new ArrayList<>();

	@Override
	public < JsonType extends JsonElement > JsonType writeJson( Type value, JsonType json ) {
		JsonObject jsonObject = json.getAsJsonObject();
		for( ReaderKey< Type, ? > reader : this.readers ) {
			JsonElement subjson = reader.writeJson( value );
			if( subjson != null ) {
				jsonObject.add( reader.id, subjson );
			}
		}

		return json;
	}

	@Override
	public FriendlyByteBuf writeBuffer( Type value, FriendlyByteBuf buffer ) {
		for( ReaderKey< Type, ? > reader : this.readers ) {
			reader.writeBuffer( buffer, value );
		}

		return buffer;
	}

	@Override
	public < TagType extends Tag > TagType writeTag( Type value, TagType tag ) {
		CompoundTag compoundTag = ( CompoundTag )tag;
		for( ReaderKey< Type, ? > reader : this.readers ) {
			Tag subtag = reader.writeTag( value );
			if( subtag != null ) {
				compoundTag.put( reader.id, subtag );
			}
		}

		return tag;
	}

	@Override
	public Type readJson( Type value, JsonElement json ) {
		JsonObject jsonObject = json.getAsJsonObject();
		for( ReaderKey< Type, ? > reader : this.readers ) {
			if( jsonObject.has( reader.id ) ) {
				reader.readJson( value, jsonObject.get( reader.id ) );
			}
		}

		return value;
	}

	@Override
	public Type readBuffer( Type value, FriendlyByteBuf buffer ) {
		for( ReaderKey< Type, ? > reader : this.readers ) {
			reader.readBuffer( value, buffer );
		}

		return value;
	}

	@Override
	public Type readTag( Type value, Tag tag ) {
		CompoundTag compoundTag = ( CompoundTag )tag;
		for( ReaderKey< Type, ? > reader : this.readers ) {
			if( compoundTag.contains( reader.id ) ) {
				reader.readTag( value, compoundTag.get( reader.id ) );
			}
		}

		return value;
	}

	public < ValueType > SerializableObject< Type > define( String id, IReader< ValueType > reader, Function< Type, ValueType > getter,
		BiConsumer< Type, ValueType > setter
	) {
		this.readers.add( new ReaderKey<>( id, reader, getter, setter ) );

		return this;
	}
}
