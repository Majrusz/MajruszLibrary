package com.mlib.data;

import com.google.gson.JsonElement;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public abstract class SerializableStructure implements ISerializable {
	final List< ISerializable > serializableList = new ArrayList<>();

	@Override
	public void read( JsonElement element ) {
		this.serializableList.forEach( serializable->serializable.read( element ) );
	}

	@Override
	public void write( FriendlyByteBuf buffer ) {
		this.serializableList.forEach( serializable->serializable.write( buffer ) );
	}

	@Override
	public void read( FriendlyByteBuf buffer ) {
		this.serializableList.forEach( serializable->serializable.read( buffer ) );
	}

	@Override
	public void write( Tag tag ) {
		this.serializableList.forEach( serializable->serializable.write( tag ) );
	}

	@Override
	public void read( Tag tag ) {
		this.serializableList.forEach( serializable->serializable.read( tag ) );
	}

	public void define( String key, ReaderBlockPos.Getter getter, ReaderBlockPos.Setter setter ) {
		this.serializableList.add( new DataObject<>( getter, setter, new ReaderBlockPos(), key ) );
	}

	public void define( String key, ReaderBlockPos.ListGetter getter, ReaderBlockPos.ListSetter setter ) {
		this.serializableList.add( new DataObject<>( getter, setter, new ReaderList<>( new DataList<>( getter, setter, new ReaderBlockPos() ) ), key ) );
	}

	public void define( String key, ReaderBlockPos.MapGetter getter, ReaderBlockPos.MapSetter setter ) {
		this.serializableList.add( new DataObject<>( getter, setter, new ReaderMap<>( new DataMap<>( getter, setter, new ReaderBlockPos() ) ), key ) );
	}

	public void define( String key, ReaderBoolean.Getter getter, ReaderBoolean.Setter setter ) {
		this.serializableList.add( new DataObject<>( getter, setter, new ReaderBoolean(), key ) );
	}

	public void define( String key, ReaderBoolean.ListGetter getter, ReaderBoolean.ListSetter setter ) {
		this.serializableList.add( new DataObject<>( getter, setter, new ReaderList<>( new DataList<>( getter, setter, new ReaderBoolean() ) ), key ) );
	}

	public void define( String key, ReaderBoolean.MapGetter getter, ReaderBoolean.MapSetter setter ) {
		this.serializableList.add( new DataObject<>( getter, setter, new ReaderMap<>( new DataMap<>( getter, setter, new ReaderBoolean() ) ), key ) );
	}

	public < Type extends ISerializable > void define( String key, ReaderCustom.Getter< Type > getter, ReaderCustom.Setter< Type > setter,
		Supplier< Type > newInstance
	) {
		this.serializableList.add( new DataObject<>( getter, setter, new ReaderCustom<>( newInstance ), key ) );
	}

	public < Type extends ISerializable > void define( String key, ReaderCustom.ListGetter< Type > getter, ReaderCustom.ListSetter< Type > setter,
		Supplier< Type > newInstance
	) {
		this.serializableList.add( new DataObject<>( getter, setter, new ReaderList<>( new DataList<>( getter, setter, new ReaderCustom<>( newInstance ) ) ), key ) );
	}

	public < Type extends ISerializable > void define( String key, ReaderCustom.MapGetter< Type > getter, ReaderCustom.MapSetter< Type > setter,
		Supplier< Type > newInstance
	) {
		this.serializableList.add( new DataObject<>( getter, setter, new ReaderMap<>( new DataMap<>( getter, setter, new ReaderCustom<>( newInstance ) ) ), key ) );
	}

	public void define( String key, ReaderEnchantment.Getter getter, ReaderEnchantment.Setter setter ) {
		this.serializableList.add( new DataObject<>( getter, setter, new ReaderEnchantment(), key ) );
	}

	public void define( String key, ReaderEnchantment.ListGetter getter, ReaderEnchantment.ListSetter setter ) {
		this.serializableList.add( new DataObject<>( getter, setter, new ReaderList<>( new DataList<>( getter, setter, new ReaderEnchantment() ) ), key ) );
	}

	public void define( String key, ReaderEnchantment.MapGetter getter, ReaderEnchantment.MapSetter setter ) {
		this.serializableList.add( new DataObject<>( getter, setter, new ReaderMap<>( new DataMap<>( getter, setter, new ReaderEnchantment() ) ), key ) );
	}

	public void define( String key, ReaderEntityType.Getter getter, ReaderEntityType.Setter setter ) {
		this.serializableList.add( new DataObject<>( getter, setter, new ReaderEntityType(), key ) );
	}

	public void define( String key, ReaderEntityType.ListGetter getter, ReaderEntityType.ListSetter setter ) {
		this.serializableList.add( new DataObject<>( getter, setter, new ReaderList<>( new DataList<>( getter, setter, new ReaderEntityType() ) ), key ) );
	}

	public void define( String key, ReaderEntityType.MapGetter getter, ReaderEntityType.MapSetter setter ) {
		this.serializableList.add( new DataObject<>( getter, setter, new ReaderMap<>( new DataMap<>( getter, setter, new ReaderEntityType() ) ), key ) );
	}

	public < Type extends Enum< ? > > void define( String key, ReaderEnum.Getter< Type > getter, ReaderEnum.Setter< Type > setter, Supplier< Type[] > values ) {
		this.serializableList.add( new DataObject<>( getter, setter, new ReaderEnum<>( values ), key ) );
	}

	public < Type extends Enum< ? > > void define( String key, ReaderEnum.ListGetter< Type > getter, ReaderEnum.ListSetter< Type > setter,
		Supplier< Type[] > values
	) {
		this.serializableList.add( new DataObject<>( getter, setter, new ReaderList<>( new DataList<>( getter, setter, new ReaderEnum<>( values ) ) ), key ) );
	}

	public < Type extends Enum< ? > > void define( String key, ReaderEnum.MapGetter< Type > getter, ReaderEnum.MapSetter< Type > setter,
		Supplier< Type[] > values
	) {
		this.serializableList.add( new DataObject<>( getter, setter, new ReaderMap<>( new DataMap<>( getter, setter, new ReaderEnum<>( values ) ) ), key ) );
	}

	public void define( String key, ReaderFloat.Getter getter, ReaderFloat.Setter setter ) {
		this.serializableList.add( new DataObject<>( getter, setter, new ReaderFloat(), key ) );
	}

	public void define( String key, ReaderFloat.ListGetter getter, ReaderFloat.ListSetter setter ) {
		this.serializableList.add( new DataObject<>( getter, setter, new ReaderList<>( new DataList<>( getter, setter, new ReaderFloat() ) ), key ) );
	}

	public void define( String key, ReaderFloat.MapGetter getter, ReaderFloat.MapSetter setter ) {
		this.serializableList.add( new DataObject<>( getter, setter, new ReaderMap<>( new DataMap<>( getter, setter, new ReaderFloat() ) ), key ) );
	}

	public void define( String key, ReaderInteger.Getter getter, ReaderInteger.Setter setter ) {
		this.serializableList.add( new DataObject<>( getter, setter, new ReaderInteger(), key ) );
	}

	public void define( String key, ReaderInteger.ListGetter getter, ReaderInteger.ListSetter setter ) {
		this.serializableList.add( new DataObject<>( getter, setter, new ReaderList<>( new DataList<>( getter, setter, new ReaderInteger() ) ), key ) );
	}

	public void define( String key, ReaderInteger.MapGetter getter, ReaderInteger.MapSetter setter ) {
		this.serializableList.add( new DataObject<>( getter, setter, new ReaderMap<>( new DataMap<>( getter, setter, new ReaderInteger() ) ), key ) );
	}

	public void define( String key, ReaderResourceLocation.Getter getter, ReaderResourceLocation.Setter setter ) {
		this.serializableList.add( new DataObject<>( getter, setter, new ReaderResourceLocation(), key ) );
	}

	public void define( String key, ReaderResourceLocation.ListGetter getter, ReaderResourceLocation.ListSetter setter ) {
		this.serializableList.add( new DataObject<>( getter, setter, new ReaderList<>( new DataList<>( getter, setter, new ReaderResourceLocation() ) ), key ) );
	}

	public void define( String key, ReaderResourceLocation.MapGetter getter, ReaderResourceLocation.MapSetter setter ) {
		this.serializableList.add( new DataObject<>( getter, setter, new ReaderMap<>( new DataMap<>( getter, setter, new ReaderResourceLocation() ) ), key ) );
	}

	public void define( String key, ReaderString.Getter getter, ReaderString.Setter setter ) {
		this.serializableList.add( new DataObject<>( getter, setter, new ReaderString(), key ) );
	}

	public void define( String key, ReaderString.ListGetter getter, ReaderString.ListSetter setter ) {
		this.serializableList.add( new DataObject<>( getter, setter, new ReaderList<>( new DataList<>( getter, setter, new ReaderString() ) ), key ) );
	}

	public void define( String key, ReaderString.MapGetter getter, ReaderString.MapSetter setter ) {
		this.serializableList.add( new DataObject<>( getter, setter, new ReaderMap<>( new DataMap<>( getter, setter, new ReaderString() ) ), key ) );
	}

	public void define( String key, ReaderUUID.Getter getter, ReaderUUID.Setter setter ) {
		this.serializableList.add( new DataObject<>( getter, setter, new ReaderUUID(), key ) );
	}

	public void define( String key, ReaderUUID.ListGetter getter, ReaderUUID.ListSetter setter ) {
		this.serializableList.add( new DataObject<>( getter, setter, new ReaderList<>( new DataList<>( getter, setter, new ReaderUUID() ) ), key ) );
	}

	public void define( String key, ReaderUUID.MapGetter getter, ReaderUUID.MapSetter setter ) {
		this.serializableList.add( new DataObject<>( getter, setter, new ReaderMap<>( new DataMap<>( getter, setter, new ReaderUUID() ) ), key ) );
	}
}
