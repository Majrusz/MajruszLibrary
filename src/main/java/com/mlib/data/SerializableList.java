package com.mlib.data;

import com.google.gson.JsonElement;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.Supplier;

public abstract class SerializableList implements ISerializable {
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

	public void define( ReaderBlockPos.ListGetter getter, ReaderBlockPos.ListSetter setter ) {
		this.serializable = new DataList<>( getter, setter, new ReaderBlockPos() );
	}

	public void define( ReaderBoolean.ListGetter getter, ReaderBoolean.ListSetter setter ) {
		this.serializable = new DataList<>( getter, setter, new ReaderBoolean() );
	}

	public < Type extends ISerializable > void define( ReaderCustom.ListGetter< Type > getter, ReaderCustom.ListSetter< Type > setter,
		Supplier< Type > newInstance
	) {
		this.serializable = new DataList<>( getter, setter, new ReaderCustom<>( newInstance ) );
	}

	public void define( ReaderEnchantment.ListGetter getter, ReaderEnchantment.ListSetter setter ) {
		this.serializable = new DataList<>( getter, setter, new ReaderEnchantment() );
	}

	public void define( ReaderEntityType.ListGetter getter, ReaderEntityType.ListSetter setter ) {
		this.serializable = new DataList<>( getter, setter, new ReaderEntityType() );
	}

	public < Type extends Enum< ? > > void define( ReaderEnum.ListGetter< Type > getter, ReaderEnum.ListSetter< Type > setter, Supplier< Type[] > values ) {
		this.serializable = new DataList<>( getter, setter, new ReaderEnum<>( values ) );
	}

	public void define( ReaderFloat.ListGetter getter, ReaderFloat.ListSetter setter ) {
		this.serializable = new DataList<>( getter, setter, new ReaderFloat() );
	}

	public void define( ReaderInteger.ListGetter getter, ReaderInteger.ListSetter setter ) {
		this.serializable = new DataList<>( getter, setter, new ReaderInteger() );
	}

	public void define( ReaderResourceLocation.ListGetter getter, ReaderResourceLocation.ListSetter setter ) {
		this.serializable = new DataList<>( getter, setter, new ReaderResourceLocation() );
	}

	public void define( ReaderString.ListGetter getter, ReaderString.ListSetter setter ) {
		this.serializable = new DataList<>( getter, setter, new ReaderString() );
	}

	public void define( ReaderUUID.ListGetter getter, ReaderUUID.ListSetter setter ) {
		this.serializable = new DataList<>( getter, setter, new ReaderUUID() );
	}
}
