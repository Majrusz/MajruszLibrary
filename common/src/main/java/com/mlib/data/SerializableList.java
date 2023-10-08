package com.mlib.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.UUID;
import java.util.function.Supplier;

public abstract class SerializableList implements ISerializable {
	final String key;
	ISerializable serializable = null;

	public SerializableList( String key ) {
		this.key = key;
	}

	public SerializableList() {
		this( null );
	}

	@Override
	public void write( JsonElement element ) {
		JsonElement subelement = SerializableHelper.getWriteSubelement( element, this.key, JsonArray::new );

		this.serializable.write( subelement );
	}

	@Override
	public void read( JsonElement element ) {
		JsonElement subelement = SerializableHelper.getReadSubelement( element, this.key );
		if( subelement == null ) {
			return;
		}

		this.serializable.read( subelement );
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
		Tag subtag = SerializableHelper.getWriteSubtag( tag, this.key, ListTag::new );

		this.serializable.write( subtag );
	}

	@Override
	public void read( Tag tag ) {
		Tag subtag = SerializableHelper.getReadSubtag( tag, this.key );
		if( subtag == null ) {
			return;
		}

		this.serializable.read( subtag );
	}

	public void defineBlockPos( DataList.Getter< BlockPos > getter, DataList.Setter< BlockPos > setter ) {
		this.serializable = new DataList<>( getter, setter, new ReaderBlockPos() );
	}

	public void defineBoolean( DataList.Getter< Boolean > getter, DataList.Setter< Boolean > setter ) {
		this.serializable = new DataList<>( getter, setter, new ReaderBoolean() );
	}

	public < Type extends ISerializable > void defineCustom( DataList.Getter< Type > getter, DataList.Setter< Type > setter, Supplier< Type > newInstance ) {
		this.serializable = new DataList<>( getter, setter, new ReaderCustom<>( newInstance ) );
	}

	public void defineEnchantment( DataList.Getter< Enchantment > getter, DataList.Setter< Enchantment > setter ) {
		this.serializable = new DataList<>( getter, setter, new ReaderEnchantment() );
	}

	public void defineEntityType( DataList.Getter< EntityType< ? > > getter, DataList.Setter< EntityType< ? > > setter ) {
		this.serializable = new DataList<>( getter, setter, new ReaderEntityType() );
	}

	public < Type extends Enum< ? > > void defineEnum( DataList.Getter< Type > getter, DataList.Setter< Type > setter, Supplier< Type[] > values ) {
		this.serializable = new DataList<>( getter, setter, new ReaderEnum<>( values ) );
	}

	public void defineFloat( DataList.Getter< Float > getter, DataList.Setter< Float > setter ) {
		this.serializable = new DataList<>( getter, setter, new ReaderFloat() );
	}

	public void defineInteger( DataList.Getter< Integer > getter, DataList.Setter< Integer > setter ) {
		this.serializable = new DataList<>( getter, setter, new ReaderInteger() );
	}

	public void defineLocation( DataList.Getter< ResourceLocation > getter, DataList.Setter< ResourceLocation > setter ) {
		this.serializable = new DataList<>( getter, setter, new ReaderResourceLocation() );
	}

	public void defineString( DataList.Getter< String > getter, DataList.Setter< String > setter ) {
		this.serializable = new DataList<>( getter, setter, new ReaderString() );
	}

	public void defineUUID( DataList.Getter< UUID > getter, DataList.Setter< UUID > setter ) {
		this.serializable = new DataList<>( getter, setter, new ReaderUUID() );
	}
}
