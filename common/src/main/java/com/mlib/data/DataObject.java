package com.mlib.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.Consumer;
import java.util.function.Supplier;

record DataObject< Type >( Supplier< Type > getter, Consumer< Type > setter, IReader< Type > reader, String key ) implements ISerializable {
	@Override
	public void read( JsonElement element ) {
		JsonObject object = element.getAsJsonObject();
		if( object.has( this.key ) ) {
			this.setter.accept( this.reader.read( object.get( this.key ) ) );
		}
	}

	@Override
	public void write( FriendlyByteBuf buffer ) {
		this.reader.write( buffer, this.getter.get() );
	}

	@Override
	public void read( FriendlyByteBuf buffer ) {
		this.setter.accept( this.reader.read( buffer ) );
	}

	@Override
	public void write( Tag tag ) {
		if( this.getter.get() == null ) {
			return;
		}

		CompoundTag compoundTag = ( CompoundTag )tag;
		compoundTag.put( this.key, this.reader.write( this.getter.get() ) );
	}

	@Override
	public void read( Tag tag ) {
		CompoundTag compoundTag = ( CompoundTag )tag;
		if( compoundTag.contains( this.key ) ) {
			this.setter.accept( this.reader.read( compoundTag.get( this.key ) ) );
		}
	}

	public interface Getter< Type > extends Supplier< Type > {}

	public interface Setter< Type > extends Consumer< Type > {}
}
