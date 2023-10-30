package com.mlib.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.Supplier;

class ReaderCustom< Type extends ISerializable > implements IReader< Type > {
	final Supplier< Type > supplier;

	public ReaderCustom( Supplier< Type > supplier ) {
		this.supplier = supplier;
	}

	@Override
	public JsonElement writeJson( Type value ) {
		return SerializableHelper.write( ()->value, new JsonObject() );
	}

	@Override
	public Type readJson( JsonElement element ) {
		return SerializableHelper.read( this.supplier, element );
	}

	@Override
	public void writeBuffer( FriendlyByteBuf buffer, Type value ) {
		value.write( buffer );
	}

	@Override
	public Type readBuffer( FriendlyByteBuf buffer ) {
		return SerializableHelper.read( this.supplier, buffer );
	}

	@Override
	public Tag writeTag( Type value ) {
		return SerializableHelper.write( ()->value, new CompoundTag() );
	}

	@Override
	public Type readTag( Tag tag ) {
		return SerializableHelper.read( this.supplier, tag );
	}
}
