package com.mlib.data;

import com.google.gson.JsonElement;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

class DataBoolean extends Data< Boolean > {
	@Override
	protected JsonReader< Boolean > getJsonReader() {
		return JsonElement::getAsBoolean;
	}

	@Override
	protected BufferWriter< Boolean > getBufferWriter() {
		return FriendlyByteBuf::writeBoolean;
	}

	@Override
	protected BufferReader< Boolean > getBufferReader() {
		return FriendlyByteBuf::readBoolean;
	}

	@Override
	protected TagWriter< Boolean > getTagWriter() {
		return CompoundTag::putBoolean;
	}

	@Override
	protected TagReader< Boolean > getTagReader() {
		return CompoundTag::getBoolean;
	}
}
