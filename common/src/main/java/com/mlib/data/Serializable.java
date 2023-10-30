package com.mlib.data;

import com.google.gson.JsonElement;
import com.mlib.math.Range;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Serializable implements ISerializable {
	final List< ISerializable > serializables = new ArrayList<>();

	@Override
	public void write( JsonElement element ) {
		this.serializables.forEach( serializable->serializable.write( element ) );
	}

	@Override
	public void read( JsonElement element ) {
		this.serializables.forEach( serializable->serializable.read( element ) );
	}

	@Override
	public void write( FriendlyByteBuf buffer ) {
		this.serializables.forEach( serializable->serializable.write( buffer ) );
	}

	@Override
	public void read( FriendlyByteBuf buffer ) {
		this.serializables.forEach( serializable->serializable.read( buffer ) );
	}

	@Override
	public void write( Tag tag ) {
		this.serializables.forEach( serializable->serializable.write( tag ) );
	}

	@Override
	public void read( Tag tag ) {
		this.serializables.forEach( serializable->serializable.read( tag ) );
	}

	public void defineBlockPos( String key, DataObject.Getter< BlockPos > getter, DataObject.Setter< BlockPos > setter ) {
		this.serializables.add( new DataObject<>( getter, setter, new ReaderBlockPos(), key ) );
	}

	public void defineBlockPos( String key, DataList.Getter< BlockPos > getter, DataList.Setter< BlockPos > setter ) {
		this.serializables.add( new DataObject<>( getter, setter, new ReaderList<>( new DataList<>( getter, setter, new ReaderBlockPos() ) ), key ) );
	}

	public void defineBlockPos( String key, DataMap.Getter< BlockPos > getter, DataMap.Setter< BlockPos > setter ) {
		this.serializables.add( new DataObject<>( getter, setter, new ReaderMap<>( new DataMap<>( getter, setter, new ReaderBlockPos() ) ), key ) );
	}

	public void defineBoolean( String key, DataObject.Getter< Boolean > getter, DataObject.Setter< Boolean > setter ) {
		this.serializables.add( new DataObject<>( getter, setter, new ReaderBoolean(), key ) );
	}

	public void defineBoolean( String key, DataList.Getter< Boolean > getter, DataList.Setter< Boolean > setter ) {
		this.serializables.add( new DataObject<>( getter, setter, new ReaderList<>( new DataList<>( getter, setter, new ReaderBoolean() ) ), key ) );
	}

	public void defineBoolean( String key, DataMap.Getter< Boolean > getter, DataMap.Setter< Boolean > setter ) {
		this.serializables.add( new DataObject<>( getter, setter, new ReaderMap<>( new DataMap<>( getter, setter, new ReaderBoolean() ) ), key ) );
	}

	public void defineCustom( String key, Consumer< Serializable > consumer ) {
		Serializable structure = new Serializable();
		consumer.accept( structure );

		this.defineCustom( key, ()->structure );
	}

	public < Type extends ISerializable > void defineCustom( String key, DataObject.Getter< Type > getter ) {
		this.serializables.add( new DataObject<>( getter, x->{}, new ReaderCustom<>( getter ), key ) );
	}

	public < Type extends ISerializable > void defineCustom( String key, DataObject.Getter< Type > getter, DataObject.Setter< Type > setter,
		Supplier< Type > newInstance
	) {
		this.serializables.add( new DataObject<>( getter, setter, new ReaderCustom<>( newInstance ), key ) );
	}

	public < Type extends ISerializable > void defineCustom( String key, DataList.Getter< Type > getter, DataList.Setter< Type > setter,
		Supplier< Type > newInstance
	) {
		this.serializables.add( new DataObject<>( getter, setter, new ReaderList<>( new DataList<>( getter, setter, new ReaderCustom<>( newInstance ) ) ), key ) );
	}

	public < Type extends ISerializable > void defineCustom( String key, DataMap.Getter< Type > getter, DataMap.Setter< Type > setter,
		Supplier< Type > newInstance
	) {
		this.serializables.add( new DataObject<>( getter, setter, new ReaderMap<>( new DataMap<>( getter, setter, new ReaderCustom<>( newInstance ) ) ), key ) );
	}

	public void defineEffect( String key, DataObject.Getter< MobEffect > getter, DataObject.Setter< MobEffect > setter ) {
		this.serializables.add( new DataObject<>( getter, setter, new ReaderMobEffect(), key ) );
	}

	public void defineEffect( String key, DataList.Getter< MobEffect > getter, DataList.Setter< MobEffect > setter ) {
		this.serializables.add( new DataObject<>( getter, setter, new ReaderList<>( new DataList<>( getter, setter, new ReaderMobEffect() ) ), key ) );
	}

	public void defineEffect( String key, DataMap.Getter< MobEffect > getter, DataMap.Setter< MobEffect > setter ) {
		this.serializables.add( new DataObject<>( getter, setter, new ReaderMap<>( new DataMap<>( getter, setter, new ReaderMobEffect() ) ), key ) );
	}

	public void defineEnchantment( String key, DataObject.Getter< Enchantment > getter, DataObject.Setter< Enchantment > setter ) {
		this.serializables.add( new DataObject<>( getter, setter, new ReaderEnchantment(), key ) );
	}

	public void defineEnchantment( String key, DataList.Getter< Enchantment > getter, DataList.Setter< Enchantment > setter ) {
		this.serializables.add( new DataObject<>( getter, setter, new ReaderList<>( new DataList<>( getter, setter, new ReaderEnchantment() ) ), key ) );
	}

	public void defineEnchantment( String key, DataMap.Getter< Enchantment > getter, DataMap.Setter< Enchantment > setter ) {
		this.serializables.add( new DataObject<>( getter, setter, new ReaderMap<>( new DataMap<>( getter, setter, new ReaderEnchantment() ) ), key ) );
	}

	public void defineEntityType( String key, DataObject.Getter< EntityType< ? > > getter, DataObject.Setter< EntityType< ? > > setter ) {
		this.serializables.add( new DataObject<>( getter, setter, new ReaderEntityType(), key ) );
	}

	public void defineEntityType( String key, DataList.Getter< EntityType< ? > > getter, DataList.Setter< EntityType< ? > > setter ) {
		this.serializables.add( new DataObject<>( getter, setter, new ReaderList<>( new DataList<>( getter, setter, new ReaderEntityType() ) ), key ) );
	}

	public void defineEntityType( String key, DataMap.Getter< EntityType< ? > > getter, DataMap.Setter< EntityType< ? > > setter ) {
		this.serializables.add( new DataObject<>( getter, setter, new ReaderMap<>( new DataMap<>( getter, setter, new ReaderEntityType() ) ), key ) );
	}

	public < Type extends Enum< ? > > void defineEnum( String key, DataObject.Getter< Type > getter, DataObject.Setter< Type > setter,
		Supplier< Type[] > values
	) {
		this.serializables.add( new DataObject<>( getter, setter, new ReaderEnum<>( values ), key ) );
	}

	public < Type extends Enum< ? > > void defineEnum( String key, DataList.Getter< Type > getter, DataList.Setter< Type > setter,
		Supplier< Type[] > values
	) {
		this.serializables.add( new DataObject<>( getter, setter, new ReaderList<>( new DataList<>( getter, setter, new ReaderEnum<>( values ) ) ), key ) );
	}

	public < Type extends Enum< ? > > void defineEnum( String key, DataMap.Getter< Type > getter, DataMap.Setter< Type > setter,
		Supplier< Type[] > values
	) {
		this.serializables.add( new DataObject<>( getter, setter, new ReaderMap<>( new DataMap<>( getter, setter, new ReaderEnum<>( values ) ) ), key ) );
	}

	public void defineFloat( String key, DataObject.Getter< Float > getter, DataObject.Setter< Float > setter ) {
		this.serializables.add( new DataObject<>( getter, setter, new ReaderFloat(), key ) );
	}

	public void defineFloat( String key, DataList.Getter< Float > getter, DataList.Setter< Float > setter ) {
		this.serializables.add( new DataObject<>( getter, setter, new ReaderList<>( new DataList<>( getter, setter, new ReaderFloat() ) ), key ) );
	}

	public void defineFloat( String key, DataMap.Getter< Float > getter, DataMap.Setter< Float > setter ) {
		this.serializables.add( new DataObject<>( getter, setter, new ReaderMap<>( new DataMap<>( getter, setter, new ReaderFloat() ) ), key ) );
	}

	public void defineFloatRange( String key, DataObject.Getter< Range< Float > > getter, DataObject.Setter< Range< Float > > setter ) {
		this.serializables.add( new DataObject<>( getter, setter, new ReaderRange<>( new ReaderFloat() ), key ) );
	}

	public void defineInteger( String key, DataObject.Getter< Integer > getter, DataObject.Setter< Integer > setter ) {
		this.serializables.add( new DataObject<>( getter, setter, new ReaderInteger(), key ) );
	}

	public void defineInteger( String key, DataList.Getter< Integer > getter, DataList.Setter< Integer > setter ) {
		this.serializables.add( new DataObject<>( getter, setter, new ReaderList<>( new DataList<>( getter, setter, new ReaderInteger() ) ), key ) );
	}

	public void defineInteger( String key, DataMap.Getter< Integer > getter, DataMap.Setter< Integer > setter ) {
		this.serializables.add( new DataObject<>( getter, setter, new ReaderMap<>( new DataMap<>( getter, setter, new ReaderInteger() ) ), key ) );
	}

	public void defineIntegerRange( String key, DataObject.Getter< Range< Integer > > getter, DataObject.Setter< Range< Integer > > setter ) {
		this.serializables.add( new DataObject<>( getter, setter, new ReaderRange<>( new ReaderInteger() ), key ) );
	}

	public void defineLocation( String key, DataObject.Getter< ResourceLocation > getter, DataObject.Setter< ResourceLocation > setter ) {
		this.serializables.add( new DataObject<>( getter, setter, new ReaderResourceLocation(), key ) );
	}

	public void defineLocation( String key, DataList.Getter< ResourceLocation > getter, DataList.Setter< ResourceLocation > setter ) {
		this.serializables.add( new DataObject<>( getter, setter, new ReaderList<>( new DataList<>( getter, setter, new ReaderResourceLocation() ) ), key ) );
	}

	public void defineLocation( String key, DataMap.Getter< ResourceLocation > getter, DataMap.Setter< ResourceLocation > setter ) {
		this.serializables.add( new DataObject<>( getter, setter, new ReaderMap<>( new DataMap<>( getter, setter, new ReaderResourceLocation() ) ), key ) );
	}

	public void defineString( String key, DataObject.Getter< String > getter, DataObject.Setter< String > setter ) {
		this.serializables.add( new DataObject<>( getter, setter, new ReaderString(), key ) );
	}

	public void defineString( String key, DataList.Getter< String > getter, DataList.Setter< String > setter ) {
		this.serializables.add( new DataObject<>( getter, setter, new ReaderList<>( new DataList<>( getter, setter, new ReaderString() ) ), key ) );
	}

	public void defineString( String key, DataMap.Getter< String > getter, DataMap.Setter< String > setter ) {
		this.serializables.add( new DataObject<>( getter, setter, new ReaderMap<>( new DataMap<>( getter, setter, new ReaderString() ) ), key ) );
	}

	public void defineUUID( String key, DataObject.Getter< UUID > getter, DataObject.Setter< UUID > setter ) {
		this.serializables.add( new DataObject<>( getter, setter, new ReaderUUID(), key ) );
	}

	public void defineUUID( String key, DataList.Getter< UUID > getter, DataList.Setter< UUID > setter ) {
		this.serializables.add( new DataObject<>( getter, setter, new ReaderList<>( new DataList<>( getter, setter, new ReaderUUID() ) ), key ) );
	}

	public void defineUUID( String key, DataMap.Getter< UUID > getter, DataMap.Setter< UUID > setter ) {
		this.serializables.add( new DataObject<>( getter, setter, new ReaderMap<>( new DataMap<>( getter, setter, new ReaderUUID() ) ), key ) );
	}
}
