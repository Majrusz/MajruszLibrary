package com.mlib.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.Map;

class ReaderMap< Type > implements IReader< Map< String, Type > > {
	final DataMap< Type > data;

	public ReaderMap( DataMap< Type > data ) {
		this.data = data;
	}

	@Override
	public JsonElement writeJson( Map< String, Type > value ) {
		return SerializableHelper.write( ()->this.data, new JsonObject() );
	}

	@Override
	public Map< String, Type > readJson( JsonElement element ) {
		return SerializableHelper.read( ()->this.data, element ).getter().get();
	}

	@Override
	public void writeBuffer( FriendlyByteBuf buffer, Map< String, Type > value ) {
		this.data.write( buffer );
	}

	@Override
	public Map< String, Type > readBuffer( FriendlyByteBuf buffer ) {
		return SerializableHelper.read( ()->this.data, buffer ).getter().get();
	}

	@Override
	public Tag writeTag( Map< String, Type > value ) {
		return SerializableHelper.write( ()->this.data, new CompoundTag() );
	}

	@Override
	public Map< String, Type > readTag( Tag tag ) {
		return SerializableHelper.read( ()->this.data, tag ).getter().get();
	}
}
