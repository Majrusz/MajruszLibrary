package com.mlib.data;

import com.google.gson.JsonElement;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

class ReaderBoolean implements IReader< Boolean > {
	@Override
	public Boolean read( JsonElement element ) {
		return element.getAsBoolean();
	}

	@Override
	public void write( FriendlyByteBuf buffer, Boolean value ) {
		buffer.writeBoolean( value );
	}

	@Override
	public Boolean read( FriendlyByteBuf buffer ) {
		return buffer.readBoolean();
	}

	@Override
	public Tag write( Boolean value ) {
		return ByteTag.valueOf( value );
	}

	@Override
	public Boolean read( Tag tag ) {
		return ( ( ByteTag )tag ).getAsByte() != 0;
	}
}
