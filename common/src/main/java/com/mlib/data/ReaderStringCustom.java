package com.mlib.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

abstract class ReaderStringCustom< Type > implements IReader< Type > {
	@Override
	public JsonElement writeJson( Type value ) {
		return new JsonPrimitive( this.convert( value ) );
	}

	@Override
	public Type readJson( JsonElement json ) {
		return this.convert( json.getAsString() );
	}

	@Override
	public void writeBuffer( FriendlyByteBuf buffer, Type value ) {
		buffer.writeUtf( this.convert( value ) );
	}

	@Override
	public Type readBuffer( FriendlyByteBuf buffer ) {
		return this.convert( buffer.readUtf() );
	}

	@Override
	public Tag writeTag( Type value ) {
		return StringTag.valueOf( this.convert( value ) );
	}

	@Override
	public Type readTag( Tag tag ) {
		return this.convert( tag.getAsString() );
	}

	public abstract Type convert( String value );

	public abstract String convert( Type value );
}
