package com.mlib.data;

import com.google.gson.JsonElement;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

abstract class ReaderStringCustom< Type > implements IReader< Type > {
	@Override
	public Type read( JsonElement element ) {
		return convert( element.getAsString() );
	}

	@Override
	public void write( FriendlyByteBuf buffer, Type value ) {
		buffer.writeUtf( convert( value ) );
	}

	@Override
	public Type read( FriendlyByteBuf buffer ) {
		return convert( buffer.readUtf() );
	}

	@Override
	public Tag write( Type value ) {
		return StringTag.valueOf( convert( value ) );
	}

	@Override
	public Type read( Tag tag ) {
		return convert( tag.getAsString() );
	}

	public abstract Type convert( String value );

	public abstract String convert( Type value );
}
