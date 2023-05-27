package com.mlib.data;

import com.google.gson.JsonElement;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 This class is a wrapper for serializable data in the game.
 It allows defining variables that handle:
 - write operation to .json files
 - write and read operations to NBT structure
 - send and receive operations between the client and the server
 .
 DataXYZ.Supplier and DataXYZ.Consumer are only defined because
 Javas Virtual Machine does not allow using generics for almost
 identical methods.
 */
public abstract class SerializableStructure implements ISerializable {
	final List< ISerializable > serializableList = new ArrayList<>();
	final String key;

	public static < Type extends SerializableStructure > void register( SimpleChannel channel, int index, Class< Type > classType,
		Supplier< Type > supplier
	) {
		channel.registerMessage(
			index,
			classType,
			SerializableStructure::write,
			( buffer )->{
				Type value = supplier.get();
				value.read( buffer );

				return value;
			},
			( structure, contextSupplier )->{
				NetworkEvent.Context context = contextSupplier.get();
				context.enqueueWork( ()->{
					ServerPlayer sender = context.getSender();
					if( sender != null ) {
						structure.onServer( sender, context );
					} else {
						DistExecutor.unsafeRunWhenOn( Dist.CLIENT, ()->()->structure.onClient( context ) );
					}
				} );
				context.setPacketHandled( true );
			}
		);
	}

	public SerializableStructure( String key ) {
		this.key = key;
	}

	public SerializableStructure() {
		this( null );
	}

	@Override
	public void read( JsonElement element ) {
		if( this.key != null ) {
			JsonElement subelement = element.getAsJsonObject().get( this.key );
			this.serializableList.forEach( serializable->serializable.read( subelement ) );
		} else {
			this.serializableList.forEach( serializable->serializable.read( element ) );
		}

		this.onRead();
	}

	@Override
	public void write( FriendlyByteBuf buffer ) {
		this.serializableList.forEach( serializable->serializable.write( buffer ) );

		this.onWrite();
	}

	@Override
	public void read( FriendlyByteBuf buffer ) {
		this.serializableList.forEach( serializable->serializable.read( buffer ) );

		this.onRead();
	}

	@Override
	public void write( CompoundTag tag ) {
		if( this.key != null ) {
			CompoundTag subtag = new CompoundTag();
			this.serializableList.forEach( serializable->serializable.write( subtag ) );
			tag.put( this.key, subtag );
		} else {
			this.serializableList.forEach( serializable->serializable.write( tag ) );
		}

		this.onWrite();
	}

	@Override
	public void read( CompoundTag tag ) {
		if( this.key != null ) {
			CompoundTag subtag = tag.getCompound( this.key );
			this.serializableList.forEach( serializable->serializable.read( subtag ) );
		} else {
			this.serializableList.forEach( serializable->serializable.read( tag ) );
		}

		this.onRead();
	}

	protected void onServer( ServerPlayer sender, NetworkEvent.Context context ) {}

	@OnlyIn( Dist.CLIENT )
	protected void onClient( NetworkEvent.Context context ) {}

	protected void onWrite() {}

	protected void onRead() {}

	protected void define( String key, DataBlockPos.Supplier getter, DataBlockPos.Consumer setter ) {
		this.serializableList.add( new DataBlockPos( key, getter, setter ) );
	}

	protected void define( String key, DataBoolean.Supplier getter, DataBoolean.Consumer setter ) {
		this.serializableList.add( new DataBoolean( key, getter, setter ) );
	}

	protected void define( String key, DataEnchantment.Supplier getter, DataEnchantment.Consumer setter ) {
		this.serializableList.add( new DataEnchantment( key, getter, setter ) );
	}

	protected void define( String key, DataEntityType.Supplier getter, DataEntityType.Consumer setter ) {
		this.serializableList.add( new DataEntityType( key, getter, setter ) );
	}

	protected < Type extends Enum< ? > > void define( String key, DataEnum.Supplier< Type > getter, DataEnum.Consumer< Type > setter,
		Supplier< Type[] > values
	) {
		this.serializableList.add( new DataEnum<>( key, getter, setter, values ) );
	}

	protected void define( String key, DataFloat.Supplier getter, DataFloat.Consumer setter ) {
		this.serializableList.add( new DataFloat( key, getter, setter ) );
	}

	protected void define( String key, DataInteger.Supplier getter, DataInteger.Consumer setter ) {
		this.serializableList.add( new DataInteger( key, getter, setter ) );
	}

	protected < Type extends SerializableStructure > void define( String key, DataList.Supplier< Type > getter, DataList.Consumer< Type > setter,
		Supplier< Type > instanceProvider
	) {
		this.serializableList.add( new DataList<>( key, getter, setter, instanceProvider ) );
	}

	protected < Type extends SerializableStructure > void define( String key, DataMap.Supplier< Type > getter, DataMap.Consumer< Type > setter,
		Supplier< Type > instanceProvider
	) {
		this.serializableList.add( new DataMap<>( key, getter, setter, instanceProvider ) );
	}

	protected void define( String key, DataResourceLocation.Supplier getter, DataResourceLocation.Consumer setter ) {
		this.serializableList.add( new DataResourceLocation( key, getter, setter ) );
	}

	protected void define( String key, DataString.Supplier getter, DataString.Consumer setter ) {
		this.serializableList.add( new DataString( key, getter, setter ) );
	}

	protected < Type extends SerializableStructure > void define( String key, DataStructure.Supplier< Type > getter, DataStructure.Consumer< Type > setter,
		Supplier< Type > instanceProvider
	) {
		this.serializableList.add( new DataStructure<>( key, getter, setter, instanceProvider ) );
	}

	protected < Type extends SerializableStructure > void define( String key, DataStructure.Supplier< Type > getter ) {
		this.define( key, getter, x->{}, getter );
	}

	protected void define( String key, DataUUID.Supplier getter, DataUUID.Consumer setter ) {
		this.serializableList.add( new DataUUID( key, getter, setter ) );
	}
}
