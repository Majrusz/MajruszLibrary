package com.majruszlibrary.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.majruszlibrary.math.Range;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

class ReaderRange< Type extends Number & Comparable< Type > > implements IReader< Range< Type > > {
	IReader< Type > reader;

	public ReaderRange( IReader< Type > reader ) {
		this.reader = reader;
	}

	@Override
	public JsonElement writeJson( Range< Type > value ) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.add( "min", this.reader.writeJson( value.from ) );
		jsonObject.add( "max", this.reader.writeJson( value.to ) );

		return jsonObject;
	}

	@Override
	public void writeBuffer( FriendlyByteBuf buffer, Range< Type > value ) {
		this.reader.writeBuffer( buffer, value.from );
		this.reader.writeBuffer( buffer, value.to );
	}

	@Override
	public Tag writeTag( Range< Type > value ) {
		CompoundTag compoundTag = new CompoundTag();
		compoundTag.put( "min", this.reader.writeTag( value.from ) );
		compoundTag.put( "max", this.reader.writeTag( value.to ) );

		return compoundTag;
	}

	@Override
	public Range< Type > readJson( JsonElement json ) {
		JsonObject jsonObject = json.getAsJsonObject();

		return Range.validated(
			this.reader.readJson( jsonObject.get( "min" ) ),
			this.reader.readJson( jsonObject.get( "max" ) )
		);
	}

	@Override
	public Range< Type > readBuffer( FriendlyByteBuf buffer ) {
		return Range.validated(
			this.reader.readBuffer( buffer ),
			this.reader.readBuffer( buffer )
		);
	}

	@Override
	public Range< Type > readTag( Tag tag ) {
		CompoundTag compoundTag = ( CompoundTag )tag;

		return Range.validated(
			this.reader.readTag( compoundTag.get( "min" ) ),
			this.reader.readTag( compoundTag.get( "max" ) )
		);
	}
}
