package com.mlib.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.List;

class ReaderList< Type > implements IReader< List< Type > > {
	final DataList< Type > data;

	public ReaderList( DataList< Type > data ) {
		this.data = data;
	}

	@Override
	public JsonElement writeJson( List< Type > value ) {
		return SerializableHelper.write( ()->this.data, new JsonArray() );
	}

	@Override
	public List< Type > readJson( JsonElement element ) {
		return SerializableHelper.read( ()->this.data, element ).getter().get();
	}

	@Override
	public void writeBuffer( FriendlyByteBuf buffer, List< Type > value ) {
		this.data.write( buffer );
	}

	@Override
	public List< Type > readBuffer( FriendlyByteBuf buffer ) {
		return SerializableHelper.read( ()->this.data, buffer ).getter().get();
	}

	@Override
	public Tag writeTag( List< Type > value ) {
		return SerializableHelper.write( ()->this.data, new ListTag() );
	}

	@Override
	public List< Type > readTag( Tag tag ) {
		return SerializableHelper.read( ()->this.data, tag ).getter().get();
	}
}
