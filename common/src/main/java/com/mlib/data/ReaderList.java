package com.mlib.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.ArrayList;
import java.util.List;

class ReaderList< Type > implements IReader< List< Type > > {
	private final IReader< Type > reader;

	public ReaderList( IReader< Type > reader ) {
		this.reader = reader;
	}

	@Override
	public JsonElement writeJson( List< Type > values ) {
		JsonArray jsonArray = new JsonArray();
		for( Type value : values ) {
			jsonArray.add( this.reader.writeJson( value ) );
		}

		return jsonArray;
	}

	@Override
	public void writeBuffer( FriendlyByteBuf buffer, List< Type > values ) {
		buffer.writeCollection( values, this.reader::writeBuffer );
	}

	@Override
	public Tag writeTag( List< Type > values ) {
		ListTag listTag = new ListTag();
		for( Type value : values ) {
			listTag.add( this.reader.writeTag( value ) );
		}

		return listTag;
	}

	@Override
	public List< Type > readJson( JsonElement json ) {
		List< Type > values = new ArrayList<>();
		for( JsonElement subjson : json.getAsJsonArray() ) {
			values.add( this.reader.readJson( subjson ) );
		}

		return values;
	}

	@Override
	public List< Type > readBuffer( FriendlyByteBuf buffer ) {
		return buffer.readCollection( ArrayList::new, this.reader::readBuffer );
	}

	@Override
	public List< Type > readTag( Tag tag ) {
		List< Type > values = new ArrayList<>();
		for( Tag subtag : ( ListTag )tag ) {
			values.add( this.reader.readTag( subtag ) );
		}

		return values;
	}
}
