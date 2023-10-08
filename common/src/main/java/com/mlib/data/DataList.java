package com.mlib.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

record DataList< Type >( Getter< Type > getter, Setter< Type > setter, IReader< Type > reader ) implements ISerializable {
	@Override
	public void write( JsonElement element ) {
		JsonArray array = element.getAsJsonArray();
		for( Type value : this.getter.get() ) {
			array.add( this.reader.writeJson( value ) );
		}
	}

	@Override
	public void read( JsonElement element ) {
		JsonArray array = element.getAsJsonArray();
		List< Type > values = new ArrayList<>();
		for( JsonElement subelement : array ) {
			values.add( this.reader.readJson( subelement ) );
		}

		this.setter.accept( values );
	}

	@Override
	public void write( FriendlyByteBuf buffer ) {
		buffer.writeCollection( this.getter.get(), this.reader::writeBuffer );
	}

	@Override
	public void read( FriendlyByteBuf buffer ) {
		this.setter.accept( buffer.readCollection( ArrayList::new, this.reader::readBuffer ) );
	}

	@Override
	public void write( Tag tag ) {
		ListTag listTag = ( ListTag )tag;
		List< Type > values = this.getter.get();
		for( Type value : values ) {
			listTag.add( this.reader.writeTag( value ) );
		}
	}

	@Override
	public void read( Tag tag ) {
		ListTag listTag = ( ListTag )tag;
		List< Type > values = new ArrayList<>();
		for( Tag subtag : listTag ) {
			values.add( this.reader.readTag( subtag ) );
		}

		this.setter.accept( values );
	}

	public interface Getter< Type > extends Supplier< List< Type > > {}

	public interface Setter< Type > extends Consumer< List< Type > > {}
}
