package net.mlib.data;

import com.google.gson.JsonElement;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.UUID;

class ReaderUUID implements IReader< UUID > {
	@Override
	public UUID read( JsonElement element ) {
		return UUID.fromString( element.getAsString() );
	}

	@Override
	public void write( FriendlyByteBuf buffer, UUID value ) {
		buffer.writeLong( value.getMostSignificantBits() );
		buffer.writeLong( value.getLeastSignificantBits() );
	}

	@Override
	public UUID read( FriendlyByteBuf buffer ) {
		return new UUID( buffer.readLong(), buffer.readLong() );
	}

	@Override
	public Tag write( UUID value ) {
		return NbtUtils.createUUID( value );
	}

	@Override
	public UUID read( Tag tag ) {
		return NbtUtils.loadUUID( tag );
	}
}
