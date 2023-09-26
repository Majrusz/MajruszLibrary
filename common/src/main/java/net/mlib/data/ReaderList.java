package net.mlib.data;

import com.google.gson.JsonElement;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.List;

class ReaderList< Type > implements IReader< List< Type > > {
	final DataList< Type > data;

	public ReaderList( DataList< Type > data ) {
		this.data = data;
	}

	@Override
	public List< Type > read( JsonElement element ) {
		return SerializableHelper.read( ()->this.data, element ).getter().get();
	}

	@Override
	public void write( FriendlyByteBuf buffer, List< Type > value ) {
		this.data.write( buffer );
	}

	@Override
	public List< Type > read( FriendlyByteBuf buffer ) {
		return SerializableHelper.read( ()->this.data, buffer ).getter().get();
	}

	@Override
	public Tag write( List< Type > value ) {
		return SerializableHelper.write( ()->this.data, new ListTag() );
	}

	@Override
	public List< Type > read( Tag tag ) {
		return SerializableHelper.read( ()->this.data, tag ).getter().get();
	}
}
