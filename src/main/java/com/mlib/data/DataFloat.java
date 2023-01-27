package com.mlib.data;

import com.google.gson.JsonElement;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

class DataFloat extends Data< Float > {
	public DataFloat( String key, Supplier getter, Consumer setter ) {
		super( key, getter, setter );
	}

	@Override
	protected JsonReader< Float > getJsonReader() {
		return JsonElement::getAsFloat;
	}

	@Override
	protected BufferWriter< Float > getBufferWriter() {
		return FriendlyByteBuf::writeFloat;
	}

	@Override
	protected BufferReader< Float > getBufferReader() {
		return FriendlyByteBuf::readFloat;
	}

	@Override
	protected TagWriter< Float > getTagWriter() {
		return CompoundTag::putFloat;
	}

	@Override
	protected TagReader< Float > getTagReader() {
		return CompoundTag::getFloat;
	}

	@FunctionalInterface
	public interface Supplier extends java.util.function.Supplier< Float > {}

	@FunctionalInterface
	public interface Consumer extends java.util.function.Consumer< Float > {}
}
