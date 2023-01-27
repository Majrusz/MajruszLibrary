package com.mlib.data;

import com.google.gson.JsonElement;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

class DataInteger extends Data< Integer > {
	public DataInteger( String key, Supplier getter, Consumer setter ) {
		super( key, getter, setter );
	}

	@Override
	protected JsonReader< Integer > getJsonReader() {
		return JsonElement::getAsInt;
	}

	@Override
	protected BufferWriter< Integer > getBufferWriter() {
		return FriendlyByteBuf::writeInt;
	}

	@Override
	protected BufferReader< Integer > getBufferReader() {
		return FriendlyByteBuf::readInt;
	}

	@Override
	protected TagWriter< Integer > getTagWriter() {
		return CompoundTag::putInt;
	}

	@Override
	protected TagReader< Integer > getTagReader() {
		return CompoundTag::getInt;
	}

	@FunctionalInterface
	public interface Supplier extends java.util.function.Supplier< Integer > {}

	@FunctionalInterface
	public interface Consumer extends java.util.function.Consumer< Integer > {}
}
