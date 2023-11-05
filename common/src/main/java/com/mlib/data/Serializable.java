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

public final class Serializable< Type > implements ISerializable< Type > {
	final List< ISerializable< Type > > serializables = new ArrayList<>();

	@Override
	public void write( Type object, JsonElement json ) {
		this.serializables.forEach( serializable->serializable.write( object, json ) );
	}

	@Override
	public void read( Type object, JsonElement json ) {
		this.serializables.forEach( serializable->serializable.read( object, json ) );
	}

	@Override
	public void write( Type object, FriendlyByteBuf buffer ) {
		this.serializables.forEach( serializable->serializable.write( object, buffer ) );
	}

	@Override
	public void read( Type object, FriendlyByteBuf buffer ) {
		this.serializables.forEach( serializable->serializable.read( object, buffer ) );
	}

	@Override
	public void write( Type object, Tag tag ) {
		this.serializables.forEach( serializable->serializable.write( object, tag ) );
	}

	@Override
	public void read( Type object, Tag tag ) {
		this.serializables.forEach( serializable->serializable.read( object, tag ) );
	}

	public Serializable< Type > defineBlockPos( String key, DataObject.Getter< Type, BlockPos > getter, DataObject.Setter< Type, BlockPos > setter ) {
		this.serializables.add( new DataObject<>( getter, setter, new ReaderBlockPos(), key ) );

		return this;
	}

	public Serializable< Type > defineBlockPosList( String key, DataList.Getter< Type, BlockPos > getter, DataList.Setter< Type, BlockPos > setter ) {
		this.serializables.add( new DataList<>( getter, setter, new ReaderBlockPos(), key ) );

		return this;
	}

	public Serializable< Type > defineBlockPosMap( String key, DataMap.Getter< Type, BlockPos > getter, DataMap.Setter< Type, BlockPos > setter ) {
		this.serializables.add( new DataMap<>( getter, setter, new ReaderBlockPos(), key ) );

		return this;
	}

	public Serializable< Type > defineBoolean( String key, DataObject.Getter< Type, Boolean > getter, DataObject.Setter< Type, Boolean > setter ) {
		this.serializables.add( new DataObject<>( getter, setter, new ReaderBoolean(), key ) );

		return this;
	}

	public Serializable< Type > defineBooleanList( String key, DataList.Getter< Type, Boolean > getter, DataList.Setter< Type, Boolean > setter ) {
		this.serializables.add( new DataList<>( getter, setter, new ReaderBoolean(), key ) );

		return this;
	}

	public Serializable< Type > defineBooleanMap( String key, DataMap.Getter< Type, Boolean > getter, DataMap.Setter< Type, Boolean > setter ) {
		this.serializables.add( new DataMap<>( getter, setter, new ReaderBoolean(), key ) );

		return this;
	}

	public Serializable< Type > defineCustom( String key, Consumer< Serializable< ? > > consumer ) {
		Serializable< ? > structure = new Serializable<>();
		consumer.accept( structure );

		this.defineCustom( key, ()->structure );

		return this;
	}

	public < ObjectType > Serializable< Type > defineCustom( String key, Supplier< ObjectType > instance ) {
		this.serializables.add( new DataObject<>( s->instance.get(), ( s, v )->{}, new ReaderCustom<>( instance ), key ) );

		return this;
	}

	public < ObjectType > Serializable< Type > defineCustom( String key, DataObject.Getter< Type, ObjectType > getter,
		DataObject.Setter< Type, ObjectType > setter, Supplier< ObjectType > instance
	) {
		this.serializables.add( new DataObject<>( getter, setter, new ReaderCustom<>( instance ), key ) );

		return this;
	}

	public < ObjectType > Serializable< Type > defineCustomList( String key, DataList.Getter< Type, ObjectType > getter,
		DataList.Setter< Type, ObjectType > setter, Supplier< ObjectType > instance
	) {
		this.serializables.add( new DataList<>( getter, setter, new ReaderCustom<>( instance ), key ) );

		return this;
	}

	public < ObjectType > Serializable< Type > defineCustomMap( String key, DataMap.Getter< Type, ObjectType > getter,
		DataMap.Setter< Type, ObjectType > setter, Supplier< ObjectType > instance
	) {
		this.serializables.add( new DataMap<>( getter, setter, new ReaderCustom<>( instance ), key ) );

		return this;
	}

	public Serializable< Type > defineEffect( String key, DataObject.Getter< Type, MobEffect > getter, DataObject.Setter< Type, MobEffect > setter ) {
		this.serializables.add( new DataObject<>( getter, setter, new ReaderMobEffect(), key ) );

		return this;
	}

	public Serializable< Type > defineEffectList( String key, DataList.Getter< Type, MobEffect > getter, DataList.Setter< Type, MobEffect > setter ) {
		this.serializables.add( new DataList<>( getter, setter, new ReaderMobEffect(), key ) );

		return this;
	}

	public Serializable< Type > defineEffectMap( String key, DataMap.Getter< Type, MobEffect > getter, DataMap.Setter< Type, MobEffect > setter ) {
		this.serializables.add( new DataMap<>( getter, setter, new ReaderMobEffect(), key ) );

		return this;
	}

	public Serializable< Type > defineEnchantment( String key, DataObject.Getter< Type, Enchantment > getter, DataObject.Setter< Type, Enchantment > setter ) {
		this.serializables.add( new DataObject<>( getter, setter, new ReaderEnchantment(), key ) );

		return this;
	}

	public Serializable< Type > defineEnchantmentList( String key, DataList.Getter< Type, Enchantment > getter, DataList.Setter< Type, Enchantment > setter ) {
		this.serializables.add( new DataList<>( getter, setter, new ReaderEnchantment(), key ) );

		return this;
	}

	public Serializable< Type > defineEnchantmentMap( String key, DataMap.Getter< Type, Enchantment > getter, DataMap.Setter< Type, Enchantment > setter ) {
		this.serializables.add( new DataMap<>( getter, setter, new ReaderEnchantment(), key ) );

		return this;
	}

	public Serializable< Type > defineEntityType( String key, DataObject.Getter< Type, EntityType< ? > > getter,
		DataObject.Setter< Type, EntityType< ? > > setter
	) {
		this.serializables.add( new DataObject<>( getter, setter, new ReaderEntityType(), key ) );

		return this;
	}

	public Serializable< Type > defineEntityTypeList( String key, DataList.Getter< Type, EntityType< ? > > getter,
		DataList.Setter< Type, EntityType< ? > > setter
	) {
		this.serializables.add( new DataList<>( getter, setter, new ReaderEntityType(), key ) );

		return this;
	}

	public Serializable< Type > defineEntityTypeMap( String key, DataMap.Getter< Type, EntityType< ? > > getter,
		DataMap.Setter< Type, EntityType< ? > > setter
	) {
		this.serializables.add( new DataMap<>( getter, setter, new ReaderEntityType(), key ) );

		return this;
	}

	public < EnumType extends Enum< ? > > Serializable< Type > defineEnum( String key, DataObject.Getter< Type, EnumType > getter,
		DataObject.Setter< Type, EnumType > setter, Supplier< EnumType[] > values
	) {
		this.serializables.add( new DataObject<>( getter, setter, new ReaderEnum<>( values ), key ) );

		return this;
	}

	public < EnumType extends Enum< ? > > Serializable< Type > defineEnumList( String key, DataList.Getter< Type, EnumType > getter,
		DataList.Setter< Type, EnumType > setter, Supplier< EnumType[] > values
	) {
		this.serializables.add( new DataList<>( getter, setter, new ReaderEnum<>( values ), key ) );

		return this;
	}

	public < EnumType extends Enum< ? > > Serializable< Type > defineEnumMap( String key, DataMap.Getter< Type, EnumType > getter,
		DataMap.Setter< Type, EnumType > setter, Supplier< EnumType[] > values
	) {
		this.serializables.add( new DataMap<>( getter, setter, new ReaderEnum<>( values ), key ) );

		return this;
	}

	public Serializable< Type > defineFloat( String key, DataObject.Getter< Type, Float > getter, DataObject.Setter< Type, Float > setter ) {
		this.serializables.add( new DataObject<>( getter, setter, new ReaderFloat(), key ) );

		return this;
	}

	public Serializable< Type > defineFloatList( String key, DataList.Getter< Type, Float > getter, DataList.Setter< Type, Float > setter ) {
		this.serializables.add( new DataList<>( getter, setter, new ReaderFloat(), key ) );

		return this;
	}

	public Serializable< Type > defineFloatMap( String key, DataMap.Getter< Type, Float > getter, DataMap.Setter< Type, Float > setter ) {
		this.serializables.add( new DataMap<>( getter, setter, new ReaderFloat(), key ) );

		return this;
	}

	public Serializable< Type > defineFloatRange( String key, DataObject.Getter< Type, Range< Float > > getter,
		DataObject.Setter< Type, Range< Float > > setter
	) {
		this.serializables.add( new DataObject<>( getter, setter, new ReaderRange<>( new ReaderFloat() ), key ) );

		return this;
	}

	public Serializable< Type > defineInteger( String key, DataObject.Getter< Type, Integer > getter, DataObject.Setter< Type, Integer > setter ) {
		this.serializables.add( new DataObject<>( getter, setter, new ReaderInteger(), key ) );

		return this;
	}

	public Serializable< Type > defineIntegerList( String key, DataList.Getter< Type, Integer > getter, DataList.Setter< Type, Integer > setter ) {
		this.serializables.add( new DataList<>( getter, setter, new ReaderInteger(), key ) );

		return this;
	}

	public Serializable< Type > defineIntegerMap( String key, DataMap.Getter< Type, Integer > getter, DataMap.Setter< Type, Integer > setter ) {
		this.serializables.add( new DataMap<>( getter, setter, new ReaderInteger(), key ) );

		return this;
	}

	public Serializable< Type > defineIntegerRange( String key, DataObject.Getter< Type, Range< Integer > > getter,
		DataObject.Setter< Type, Range< Integer > > setter
	) {
		this.serializables.add( new DataObject<>( getter, setter, new ReaderRange<>( new ReaderInteger() ), key ) );

		return this;
	}

	public Serializable< Type > defineLocation( String key, DataObject.Getter< Type, ResourceLocation > getter,
		DataObject.Setter< Type, ResourceLocation > setter
	) {
		this.serializables.add( new DataObject<>( getter, setter, new ReaderResourceLocation(), key ) );

		return this;
	}

	public Serializable< Type > defineLocationList( String key, DataList.Getter< Type, ResourceLocation > getter,
		DataList.Setter< Type, ResourceLocation > setter
	) {
		this.serializables.add( new DataList<>( getter, setter, new ReaderResourceLocation(), key ) );

		return this;
	}

	public Serializable< Type > defineLocationMap( String key, DataMap.Getter< Type, ResourceLocation > getter,
		DataMap.Setter< Type, ResourceLocation > setter
	) {
		this.serializables.add( new DataMap<>( getter, setter, new ReaderResourceLocation(), key ) );

		return this;
	}

	public Serializable< Type > defineString( String key, DataObject.Getter< Type, String > getter, DataObject.Setter< Type, String > setter ) {
		this.serializables.add( new DataObject<>( getter, setter, new ReaderString(), key ) );

		return this;
	}

	public Serializable< Type > defineStringList( String key, DataList.Getter< Type, String > getter, DataList.Setter< Type, String > setter ) {
		this.serializables.add( new DataList<>( getter, setter, new ReaderString(), key ) );

		return this;
	}

	public Serializable< Type > defineStringMap( String key, DataMap.Getter< Type, String > getter, DataMap.Setter< Type, String > setter ) {
		this.serializables.add( new DataMap<>( getter, setter, new ReaderString(), key ) );

		return this;
	}

	public Serializable< Type > defineUUID( String key, DataObject.Getter< Type, UUID > getter, DataObject.Setter< Type, UUID > setter ) {
		this.serializables.add( new DataObject<>( getter, setter, new ReaderUUID(), key ) );

		return this;
	}

	public Serializable< Type > defineUUIDList( String key, DataList.Getter< Type, UUID > getter, DataList.Setter< Type, UUID > setter ) {
		this.serializables.add( new DataList<>( getter, setter, new ReaderUUID(), key ) );

		return this;
	}

	public Serializable< Type > defineUUIDMap( String key, DataMap.Getter< Type, UUID > getter, DataMap.Setter< Type, UUID > setter ) {
		this.serializables.add( new DataMap<>( getter, setter, new ReaderUUID(), key ) );

		return this;
	}
}
