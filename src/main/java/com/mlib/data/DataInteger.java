package com.mlib.data;

import com.google.gson.JsonElement;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

class DataInteger extends Data< Integer > {
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
}
