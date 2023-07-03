package com.mlib.data;

import com.google.gson.JsonElement;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

class ReaderCustom< Type extends ISerializable > implements IReader< Type > {
	final Supplier< Type > supplier;

	public ReaderCustom( Supplier< Type > supplier ) {
		this.supplier = supplier;
	}

	@Override
	public Type read( JsonElement element ) {
		return SerializableHelper.read( this.supplier, element );
	}

	@Override
	public void write( FriendlyByteBuf buffer, Type value ) {
		value.write( buffer );
	}

	@Override
	public Type read( FriendlyByteBuf buffer ) {
		return SerializableHelper.read( this.supplier, buffer );
	}

	@Override
	public Tag write( Type value ) {
		return SerializableHelper.write( this.supplier, new CompoundTag() );
	}

	@Override
	public Type read( Tag tag ) {
		return SerializableHelper.read( this.supplier, tag );
	}
}
