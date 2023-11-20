package com.mlib.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

class ReaderMap< Type > implements IReader< Map< String, Type > > {
	private final IReader< Type > reader;

	public ReaderMap( IReader< Type > reader ) {
		this.reader = reader;
	}

	@Override
	public JsonElement writeJson( Map< String, Type > values ) {
		JsonObject jsonObject = new JsonObject();
		for( String key : new TreeSet<>( values.keySet() ) ) {
			jsonObject.add( key, this.reader.writeJson( values.get( key ) ) );
		}

		return jsonObject;
	}

	@Override
	public void writeBuffer( FriendlyByteBuf buffer, Map< String, Type > values ) {
		buffer.writeVarInt( values.size() );
		for( String key : new TreeSet<>( values.keySet() ) ) {
			buffer.writeUtf( key );
			this.reader.writeBuffer( buffer, values.get( key ) );
		}
	}

	@Override
	public Tag writeTag( Map< String, Type > values ) {
		CompoundTag compoundTag = new CompoundTag();
		for( String key : new TreeSet<>( values.keySet() ) ) {
			compoundTag.put( key, this.reader.writeTag( values.get( key ) ) );
		}

		return compoundTag;
	}

	@Override
	public Map< String, Type > readJson( JsonElement json ) {
		Map< String, Type > values = new HashMap<>();
		for( String key : json.getAsJsonObject().keySet() ) {
			values.put( key, this.reader.readJson( json.getAsJsonObject().get( key ) ) );
		}

		return values;
	}

	@Override
	public Map< String, Type > readBuffer( FriendlyByteBuf buffer ) {
		Map< String, Type > values = new HashMap<>();
		int size = buffer.readVarInt();
		for( int idx = 0; idx < size; ++idx ) {
			String key = buffer.readUtf();
			values.put( key, this.reader.readBuffer( buffer ) );
		}

		return values;
	}

	@Override
	public Map< String, Type > readTag( Tag tag ) {
		Map< String, Type > values = new HashMap<>();
		for( String key : ( ( CompoundTag )tag ).getAllKeys() ) {
			values.put( key, this.reader.readTag( ( ( CompoundTag )tag ).get( key ) ) );
		}

		return values;
	}
}
