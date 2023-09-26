package net.mlib.data;

import com.google.gson.JsonElement;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.NumericTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

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
}
