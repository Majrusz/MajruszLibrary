package com.mlib.data;

import com.google.gson.JsonElement;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.NumericTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

class ReaderFloat implements IReader< Float > {
	@Override
	public Float read( JsonElement element ) {
		return element.getAsFloat();
	}

	@Override
	public void write( FriendlyByteBuf buffer, Float value ) {
		buffer.writeFloat( value );
	}

	@Override
	public Float read( FriendlyByteBuf buffer ) {
		return buffer.readFloat();
	}

	@Override
	public Tag write( Float value ) {
		return FloatTag.valueOf( value );
	}

	@Override
	public Float read( Tag tag ) {
		return ( ( NumericTag )tag ).getAsFloat();
	}
}
