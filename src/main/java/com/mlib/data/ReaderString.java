package com.mlib.data;

import com.google.gson.JsonElement;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

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

	public interface Getter extends Supplier< String > {}

	public interface Setter extends Consumer< String > {}

	public interface ListGetter extends Supplier< List< String > > {}

	public interface ListSetter extends Consumer< List< String > > {}

	public interface MapGetter extends Supplier< Map< String, String > > {}

	public interface MapSetter extends Consumer< Map< String, String > > {}
}
