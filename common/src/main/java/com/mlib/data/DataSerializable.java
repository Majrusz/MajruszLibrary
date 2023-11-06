package com.mlib.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.BiConsumer;
import java.util.function.Function;

record DataSerializable< Type >( Function< Type, Serializable< Type > > getter, String key ) implements ISerializable< Type > {
	@Override
	public void write( Type object, JsonElement json ) {
		if( this.getter.apply( object ) == null ) {
			return;
		}

		JsonObject jsonObject = new JsonObject();
		this.getter.apply( object ).write( object, jsonObject );
		json.getAsJsonObject().add( this.key, jsonObject );
	}

	@Override
	public void read( Type object, JsonElement json ) {
		JsonObject jsonObject = json.getAsJsonObject();
		if( jsonObject.has( this.key ) ) {
			this.getter.apply( object ).read( object, jsonObject.getAsJsonObject( this.key ) );
		}
	}

	@Override
	public void write( Type object, FriendlyByteBuf buffer ) {
		this.getter.apply( object ).write( object, buffer );
	}

	@Override
	public void read( Type object, FriendlyByteBuf buffer ) {
		this.getter.apply( object ).read( object, buffer );
	}

	@Override
	public void write( Type object, Tag tag ) {
		if( this.getter.apply( object ) == null ) {
			return;
		}

		CompoundTag compoundTag = new CompoundTag();
		this.getter.apply( object ).write( object, compoundTag );
		( ( CompoundTag )tag ).put( this.key, compoundTag );
	}

	@Override
	public void read( Type object, Tag tag ) {
		CompoundTag compoundTag = ( CompoundTag )tag;
		if( compoundTag.contains( this.key ) ) {
			this.getter.apply( object ).read( object, compoundTag.get( this.key ) );
		}
	}

	public interface Getter< ObjectType, ValueType > extends Function< ObjectType, ValueType > {}

	public interface Setter< ObjectType, ValueType > extends BiConsumer< ObjectType, ValueType > {}
}
