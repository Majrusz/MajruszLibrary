package com.mlib.data;

import com.google.gson.JsonElement;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

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

	public interface Getter extends Supplier< UUID > {}

	public interface Setter extends Consumer< UUID > {}

	public interface ListGetter extends Supplier< List< UUID > > {}

	public interface ListSetter extends Consumer< List< UUID > > {}

	public interface MapGetter extends Supplier< Map< String, UUID > > {}

	public interface MapSetter extends Consumer< Map< String, UUID > > {}
}
