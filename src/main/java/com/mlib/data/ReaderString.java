package com.mlib.data;

import com.google.gson.JsonElement;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

class ReaderString implements IReader< String > {
	@Override
	public String read( JsonElement element ) {
		return element.getAsString();
	}

	@Override
	public void write( FriendlyByteBuf buffer, String value ) {
		buffer.writeUtf( value );
	}

	@Override
	public String read( FriendlyByteBuf buffer ) {
		return buffer.readUtf();
	}

	@Override
	public Tag write( String value ) {
		return StringTag.valueOf( value );
	}

	@Override
	public String read( Tag tag ) {
		return tag.getAsString();
	}
}
