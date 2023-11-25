package com.majruszlibrary.data;

import com.google.gson.JsonElement;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

class ReaderOptional< Type > implements IReader< Type > {
	private final IReader< Type > reader;

	public ReaderOptional( IReader< Type > reader ) {
		this.reader = reader;
	}

	@Override
	public JsonElement writeJson( Type value ) {
		return value != null ? this.reader.writeJson( value ) : null;
	}

	@Override
	public void writeBuffer( FriendlyByteBuf buffer, Type value ) {
		if( value != null ) {
			buffer.writeBoolean( true );
			this.reader.writeBuffer( buffer, value );
		} else {
			buffer.writeBoolean( false );
		}
	}

	@Override
	public Tag writeTag( Type value ) {
		return value != null ? this.reader.writeTag( value ) : null;
	}

	@Override
	public Type readJson( JsonElement json ) {
		try {
			return this.reader.readJson( json );
		} catch( Exception e ) {
			return null;
		}
	}

	@Override
	public Type readBuffer( FriendlyByteBuf buffer ) {
		return buffer.readBoolean() ? this.reader.readBuffer( buffer ) : null;
	}

	@Override
	public Type readTag( Tag tag ) {
		try {
			return this.reader.readTag( tag );
		} catch( Exception e ) {
			return null;
		}
	}
}
