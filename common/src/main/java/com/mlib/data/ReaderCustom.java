package com.mlib.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.Supplier;

class ReaderCustom< Type > implements IReader< Type > {
	final Supplier< Type > supplier;

	public ReaderCustom( Supplier< Type > supplier ) {
		this.supplier = supplier;
	}

	@Override
	public JsonElement writeJson( Type value ) {
		return Serializables.write( value, new JsonObject() );
	}

	@Override
	public Type readJson( JsonElement json ) {
		return Serializables.read( this.supplier.get(), json );
	}

	@Override
	public void writeBuffer( FriendlyByteBuf buffer, Type value ) {
		Serializables.write( value, buffer );
	}

	@Override
	public Type readBuffer( FriendlyByteBuf buffer ) {
		return Serializables.read( this.supplier.get(), buffer );
	}

	@Override
	public Tag writeTag( Type value ) {
		return Serializables.write( value, new CompoundTag() );
	}

	@Override
	public Type readTag( Tag tag ) {
		return Serializables.read( this.supplier.get(), tag );
	}
}
