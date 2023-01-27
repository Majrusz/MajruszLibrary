package com.mlib.data;

import com.google.gson.JsonElement;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

class DataString extends Data< String > {
	public DataString( String key, Supplier getter, Consumer setter ) {
		super( key, getter, setter );
	}

	@Override
	protected JsonReader< String > getJsonReader() {
		return JsonElement::getAsString;
	}

	@Override
	protected BufferWriter< String > getBufferWriter() {
		return FriendlyByteBuf::writeUtf;
	}

	@Override
	protected BufferReader< String > getBufferReader() {
		return FriendlyByteBuf::readUtf;
	}

	@Override
	protected TagWriter< String > getTagWriter() {
		return CompoundTag::putString;
	}

	@Override
	protected TagReader< String > getTagReader() {
		return CompoundTag::getString;
	}

	@FunctionalInterface
	public interface Supplier extends java.util.function.Supplier< String > {}

	@FunctionalInterface
	public interface Consumer extends java.util.function.Consumer< String > {}
}
