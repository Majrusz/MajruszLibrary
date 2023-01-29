package com.mlib.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.UUID;

class DataUUID extends Data< UUID > {
	public DataUUID( String key, Supplier getter, Consumer setter ) {
		super( key, getter, setter );
	}

	@Override
	protected JsonReader< UUID > getJsonReader() {
		return element->UUID.fromString( element.getAsString() );
	}

	@Override
	protected BufferWriter< UUID > getBufferWriter() {
		return FriendlyByteBuf::writeUUID;
	}

	@Override
	protected BufferReader< UUID > getBufferReader() {
		return FriendlyByteBuf::readUUID;
	}

	@Override
	protected TagWriter< UUID > getTagWriter() {
		return CompoundTag::putUUID;
	}

	@Override
	protected TagReader< UUID > getTagReader() {
		return CompoundTag::getUUID;
	}

	@FunctionalInterface
	public interface Supplier extends java.util.function.Supplier< UUID > {}

	@FunctionalInterface
	public interface Consumer extends java.util.function.Consumer< UUID > {}
}
