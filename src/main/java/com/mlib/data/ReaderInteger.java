package com.mlib.data;

import com.google.gson.JsonElement;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.NumericTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

class ReaderInteger implements IReader< Integer > {
	@Override
	public Integer read( JsonElement element ) {
		return element.getAsInt();
	}

	@Override
	public void write( FriendlyByteBuf buffer, Integer value ) {
		buffer.writeInt( value );
	}

	@Override
	public Integer read( FriendlyByteBuf buffer ) {
		return buffer.readInt();
	}

	@Override
	public Tag write( Integer value ) {
		return IntTag.valueOf( value );
	}

	@Override
	public Integer read( Tag tag ) {
		return ( ( NumericTag )tag ).getAsInt();
	}

	public interface Getter extends Supplier< Integer > {}

	public interface Setter extends Consumer< Integer > {}

	public interface ListGetter extends Supplier< List< Integer > > {}

	public interface ListSetter extends Consumer< List< Integer > > {}

	public interface MapGetter extends Supplier< Map< String, Integer > > {}

	public interface MapSetter extends Consumer< Map< String, Integer > > {}
}