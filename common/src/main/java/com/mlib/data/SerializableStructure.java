package com.mlib.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public abstract class SerializableStructure implements ISerializable {
	final List< ISerializable > serializableList = new ArrayList<>();
	final String key;

	public SerializableStructure( String key ) {
		this.key = key;
	}

	public SerializableStructure() {
		this( null );
	}

	@Override
	public void write( JsonElement element ) {
		JsonElement subelement = SerializableHelper.getWriteSubelement( element, this.key, JsonObject::new );

		this.serializableList.forEach( serializable->serializable.write( subelement ) );
	}

	@Override
	public void read( JsonElement element ) {
		JsonElement subelement = SerializableHelper.getReadSubelement( element, this.key );
		if( subelement == null ) {
			return;
		}

		this.serializableList.forEach( serializable->serializable.read( subelement ) );
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
		Tag subtag = SerializableHelper.getWriteSubtag( tag, this.key, CompoundTag::new );

		this.serializableList.forEach( serializable->serializable.write( subtag ) );
	}

	@Override
	public void read( Tag tag ) {
		Tag subtag = SerializableHelper.getReadSubtag( tag, this.key );
		if( subtag == null ) {
			return;
		}

		this.serializableList.forEach( serializable->serializable.read( subtag ) );
	}

	public void defineBlockPos( String key, DataObject.Getter< BlockPos > getter, DataObject.Setter< BlockPos > setter ) {
		this.serializableList.add( new DataObject<>( getter, setter, new ReaderBlockPos(), key ) );
	}

	public void defineBlockPos( String key, DataList.Getter< BlockPos > getter, DataList.Setter< BlockPos > setter ) {
		this.serializableList.add( new DataObject<>( getter, setter, new ReaderList<>( new DataList<>( getter, setter, new ReaderBlockPos() ) ), key ) );
	}

	public void defineBlockPos( String key, DataMap.Getter< BlockPos > getter, DataMap.Setter< BlockPos > setter ) {
		this.serializableList.add( new DataObject<>( getter, setter, new ReaderMap<>( new DataMap<>( getter, setter, new ReaderBlockPos() ) ), key ) );
	}

	public void defineBoolean( String key, DataObject.Getter< Boolean > getter, DataObject.Setter< Boolean > setter ) {
		this.serializableList.add( new DataObject<>( getter, setter, new ReaderBoolean(), key ) );
	}

	public void defineBoolean( String key, DataList.Getter< Boolean > getter, DataList.Setter< Boolean > setter ) {
		this.serializableList.add( new DataObject<>( getter, setter, new ReaderList<>( new DataList<>( getter, setter, new ReaderBoolean() ) ), key ) );
	}

	public void defineBoolean( String key, DataMap.Getter< Boolean > getter, DataMap.Setter< Boolean > setter ) {
		this.serializableList.add( new DataObject<>( getter, setter, new ReaderMap<>( new DataMap<>( getter, setter, new ReaderBoolean() ) ), key ) );
	}

	public < Type extends ISerializable > void defineCustom( String key, DataObject.Getter< Type > getter, DataObject.Setter< Type > setter,
		Supplier< Type > newInstance
	) {
		this.serializableList.add( new DataObject<>( getter, setter, new ReaderCustom<>( newInstance ), key ) );
	}

	public < Type extends ISerializable > void defineCustom( String key, DataList.Getter< Type > getter, DataList.Setter< Type > setter,
		Supplier< Type > newInstance
	) {
		this.serializableList.add( new DataObject<>( getter, setter, new ReaderList<>( new DataList<>( getter, setter, new ReaderCustom<>( newInstance ) ) ), key ) );
	}

	public < Type extends ISerializable > void defineCustom( String key, DataMap.Getter< Type > getter, DataMap.Setter< Type > setter,
		Supplier< Type > newInstance
	) {
		this.serializableList.add( new DataObject<>( getter, setter, new ReaderMap<>( new DataMap<>( getter, setter, new ReaderCustom<>( newInstance ) ) ), key ) );
	}

	public void defineEnchantment( String key, DataObject.Getter< Enchantment > getter, DataObject.Setter< Enchantment > setter ) {
		this.serializableList.add( new DataObject<>( getter, setter, new ReaderEnchantment(), key ) );
	}

	public void defineEnchantment( String key, DataList.Getter< Enchantment > getter, DataList.Setter< Enchantment > setter ) {
		this.serializableList.add( new DataObject<>( getter, setter, new ReaderList<>( new DataList<>( getter, setter, new ReaderEnchantment() ) ), key ) );
	}

	public void defineEnchantment( String key, DataMap.Getter< Enchantment > getter, DataMap.Setter< Enchantment > setter ) {
		this.serializableList.add( new DataObject<>( getter, setter, new ReaderMap<>( new DataMap<>( getter, setter, new ReaderEnchantment() ) ), key ) );
	}

	public void defineEntityType( String key, DataObject.Getter< EntityType< ? > > getter, DataObject.Setter< EntityType< ? > > setter ) {
		this.serializableList.add( new DataObject<>( getter, setter, new ReaderEntityType(), key ) );
	}

	public void defineEntityType( String key, DataList.Getter< EntityType< ? > > getter, DataList.Setter< EntityType< ? > > setter ) {
		this.serializableList.add( new DataObject<>( getter, setter, new ReaderList<>( new DataList<>( getter, setter, new ReaderEntityType() ) ), key ) );
	}

	public void defineEntityType( String key, DataMap.Getter< EntityType< ? > > getter, DataMap.Setter< EntityType< ? > > setter ) {
		this.serializableList.add( new DataObject<>( getter, setter, new ReaderMap<>( new DataMap<>( getter, setter, new ReaderEntityType() ) ), key ) );
	}

	public < Type extends Enum< ? > > void defineEnum( String key, DataObject.Getter< Type > getter, DataObject.Setter< Type > setter,
		Supplier< Type[] > values
	) {
		this.serializableList.add( new DataObject<>( getter, setter, new ReaderEnum<>( values ), key ) );
	}

	public < Type extends Enum< ? > > void defineEnum( String key, DataList.Getter< Type > getter, DataList.Setter< Type > setter,
		Supplier< Type[] > values
	) {
		this.serializableList.add( new DataObject<>( getter, setter, new ReaderList<>( new DataList<>( getter, setter, new ReaderEnum<>( values ) ) ), key ) );
	}

	public < Type extends Enum< ? > > void defineEnum( String key, DataMap.Getter< Type > getter, DataMap.Setter< Type > setter,
		Supplier< Type[] > values
	) {
		this.serializableList.add( new DataObject<>( getter, setter, new ReaderMap<>( new DataMap<>( getter, setter, new ReaderEnum<>( values ) ) ), key ) );
	}

	public void defineFloat( String key, DataObject.Getter< Float > getter, DataObject.Setter< Float > setter ) {
		this.serializableList.add( new DataObject<>( getter, setter, new ReaderFloat(), key ) );
	}

	public void defineFloat( String key, DataList.Getter< Float > getter, DataList.Setter< Float > setter ) {
		this.serializableList.add( new DataObject<>( getter, setter, new ReaderList<>( new DataList<>( getter, setter, new ReaderFloat() ) ), key ) );
	}

	public void defineFloat( String key, DataMap.Getter< Float > getter, DataMap.Setter< Float > setter ) {
		this.serializableList.add( new DataObject<>( getter, setter, new ReaderMap<>( new DataMap<>( getter, setter, new ReaderFloat() ) ), key ) );
	}

	public void defineInteger( String key, DataObject.Getter< Integer > getter, DataObject.Setter< Integer > setter ) {
		this.serializableList.add( new DataObject<>( getter, setter, new ReaderInteger(), key ) );
	}

	public void defineInteger( String key, DataList.Getter< Integer > getter, DataList.Setter< Integer > setter ) {
		this.serializableList.add( new DataObject<>( getter, setter, new ReaderList<>( new DataList<>( getter, setter, new ReaderInteger() ) ), key ) );
	}

	public void defineInteger( String key, DataMap.Getter< Integer > getter, DataMap.Setter< Integer > setter ) {
		this.serializableList.add( new DataObject<>( getter, setter, new ReaderMap<>( new DataMap<>( getter, setter, new ReaderInteger() ) ), key ) );
	}

	public void defineLocation( String key, DataObject.Getter< ResourceLocation > getter, DataObject.Setter< ResourceLocation > setter ) {
		this.serializableList.add( new DataObject<>( getter, setter, new ReaderResourceLocation(), key ) );
	}

	public void defineLocation( String key, DataList.Getter< ResourceLocation > getter, DataList.Setter< ResourceLocation > setter ) {
		this.serializableList.add( new DataObject<>( getter, setter, new ReaderList<>( new DataList<>( getter, setter, new ReaderResourceLocation() ) ), key ) );
	}

	public void defineLocation( String key, DataMap.Getter< ResourceLocation > getter, DataMap.Setter< ResourceLocation > setter ) {
		this.serializableList.add( new DataObject<>( getter, setter, new ReaderMap<>( new DataMap<>( getter, setter, new ReaderResourceLocation() ) ), key ) );
	}

	public void defineString( String key, DataObject.Getter< String > getter, DataObject.Setter< String > setter ) {
		this.serializableList.add( new DataObject<>( getter, setter, new ReaderString(), key ) );
	}

	public void defineString( String key, DataList.Getter< String > getter, DataList.Setter< String > setter ) {
		this.serializableList.add( new DataObject<>( getter, setter, new ReaderList<>( new DataList<>( getter, setter, new ReaderString() ) ), key ) );
	}

	public void defineString( String key, DataMap.Getter< String > getter, DataMap.Setter< String > setter ) {
		this.serializableList.add( new DataObject<>( getter, setter, new ReaderMap<>( new DataMap<>( getter, setter, new ReaderString() ) ), key ) );
	}

	public void defineUUID( String key, DataObject.Getter< UUID > getter, DataObject.Setter< UUID > setter ) {
		this.serializableList.add( new DataObject<>( getter, setter, new ReaderUUID(), key ) );
	}

	public void defineUUID( String key, DataList.Getter< UUID > getter, DataList.Setter< UUID > setter ) {
		this.serializableList.add( new DataObject<>( getter, setter, new ReaderList<>( new DataList<>( getter, setter, new ReaderUUID() ) ), key ) );
	}

	public void defineUUID( String key, DataMap.Getter< UUID > getter, DataMap.Setter< UUID > setter ) {
		this.serializableList.add( new DataObject<>( getter, setter, new ReaderMap<>( new DataMap<>( getter, setter, new ReaderUUID() ) ), key ) );
	}
}
