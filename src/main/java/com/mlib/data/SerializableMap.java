package com.mlib.data;

import com.google.gson.JsonElement;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.Supplier;

public abstract class SerializableMap implements ISerializable {
	ISerializable serializable = null;

	@Override
	public void read( JsonElement element ) {
		this.serializable.read( element );
	}

	@Override
	public void write( FriendlyByteBuf buffer ) {
		this.serializable.write( buffer );
	}

	@Override
	public void read( FriendlyByteBuf buffer ) {
		this.serializable.read( buffer );
	}

	@Override
	public void write( Tag tag ) {
		this.serializable.write( tag );
	}

	@Override
	public void read( Tag tag ) {
		this.serializable.read( tag );
	}

	public void define( ReaderBlockPos.MapGetter getter, ReaderBlockPos.MapSetter setter ) {
		this.serializable = new DataMap<>( getter, setter, new ReaderBlockPos() );
	}

	public void define( ReaderBoolean.MapGetter getter, ReaderBoolean.MapSetter setter ) {
		this.serializable = new DataMap<>( getter, setter, new ReaderBoolean() );
	}

	public < Type extends ISerializable > void define( ReaderCustom.MapGetter< Type > getter, ReaderCustom.MapSetter< Type > setter,
		Supplier< Type > newInstance
	) {
		this.serializable = new DataMap<>( getter, setter, new ReaderCustom<>( newInstance ) );
	}

	public void define( ReaderEnchantment.MapGetter getter, ReaderEnchantment.MapSetter setter ) {
		this.serializable = new DataMap<>( getter, setter, new ReaderEnchantment() );
	}

	public void define( ReaderEntityType.MapGetter getter, ReaderEntityType.MapSetter setter ) {
		this.serializable = new DataMap<>( getter, setter, new ReaderEntityType() );
	}

	public < Type extends Enum< ? > > void define( ReaderEnum.MapGetter< Type > getter, ReaderEnum.MapSetter< Type > setter, Supplier< Type[] > values ) {
		this.serializable = new DataMap<>( getter, setter, new ReaderEnum<>( values ) );
	}

	public void define( ReaderFloat.MapGetter getter, ReaderFloat.MapSetter setter ) {
		this.serializable = new DataMap<>( getter, setter, new ReaderFloat() );
	}

	public void define( ReaderInteger.MapGetter getter, ReaderInteger.MapSetter setter ) {
		this.serializable = new DataMap<>( getter, setter, new ReaderInteger() );
	}

	public void define( ReaderResourceLocation.MapGetter getter, ReaderResourceLocation.MapSetter setter ) {
		this.serializable = new DataMap<>( getter, setter, new ReaderResourceLocation() );
	}

	public void define( ReaderString.MapGetter getter, ReaderString.MapSetter setter ) {
		this.serializable = new DataMap<>( getter, setter, new ReaderString() );
	}

	public void define( ReaderUUID.MapGetter getter, ReaderUUID.MapSetter setter ) {
		this.serializable = new DataMap<>( getter, setter, new ReaderUUID() );
	}
}
