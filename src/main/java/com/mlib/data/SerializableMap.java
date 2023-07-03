package com.mlib.data;

import com.google.gson.JsonElement;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.UUID;
import java.util.function.Supplier;

public abstract class SerializableMap implements ISerializable {
	final String key;
	ISerializable serializable = null;

	public SerializableMap( String key ) {
		this.key = key;
	}

	public SerializableMap() {
		this( null );
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
		Tag subtag = SerializableHelper.getWriteSubtag( tag, this.key );
		if( subtag == null ) {
			return;
		}

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

	public void defineBlockPos( DataMap.Getter< BlockPos > getter, DataMap.Setter< BlockPos > setter ) {
		this.serializable = new DataMap<>( getter, setter, new ReaderBlockPos() );
	}

	public void defineBoolean( DataMap.Getter< Boolean > getter, DataMap.Setter< Boolean > setter ) {
		this.serializable = new DataMap<>( getter, setter, new ReaderBoolean() );
	}

	public < Type extends ISerializable > void defineCustom( DataMap.Getter< Type > getter, DataMap.Setter< Type > setter, Supplier< Type > newInstance ) {
		this.serializable = new DataMap<>( getter, setter, new ReaderCustom<>( newInstance ) );
	}

	public void defineEnchantment( DataMap.Getter< Enchantment > getter, DataMap.Setter< Enchantment > setter ) {
		this.serializable = new DataMap<>( getter, setter, new ReaderEnchantment() );
	}

	public void defineEntityType( DataMap.Getter< EntityType< ? > > getter, DataMap.Setter< EntityType< ? > > setter ) {
		this.serializable = new DataMap<>( getter, setter, new ReaderEntityType() );
	}

	public < Type extends Enum< ? > > void defineEnum( DataMap.Getter< Type > getter, DataMap.Setter< Type > setter, Supplier< Type[] > values ) {
		this.serializable = new DataMap<>( getter, setter, new ReaderEnum<>( values ) );
	}

	public void defineFloat( DataMap.Getter< Float > getter, DataMap.Setter< Float > setter ) {
		this.serializable = new DataMap<>( getter, setter, new ReaderFloat() );
	}

	public void defineInteger( DataMap.Getter< Integer > getter, DataMap.Setter< Integer > setter ) {
		this.serializable = new DataMap<>( getter, setter, new ReaderInteger() );
	}

	public void defineLocation( DataMap.Getter< ResourceLocation > getter, DataMap.Setter< ResourceLocation > setter ) {
		this.serializable = new DataMap<>( getter, setter, new ReaderResourceLocation() );
	}

	public void defineString( DataMap.Getter< String > getter, DataMap.Setter< String > setter ) {
		this.serializable = new DataMap<>( getter, setter, new ReaderString() );
	}

	public void defineUUID( DataMap.Getter< UUID > getter, DataMap.Setter< UUID > setter ) {
		this.serializable = new DataMap<>( getter, setter, new ReaderUUID() );
	}
}
