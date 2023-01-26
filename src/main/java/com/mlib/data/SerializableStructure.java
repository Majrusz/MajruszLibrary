package com.mlib.data;

import com.google.gson.JsonElement;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.enchantment.Enchantment;
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
 */
public abstract class SerializableStructure implements ISerializable {
	final List< ISerializable > serializableList = new ArrayList<>();

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
	public void write( CompoundTag tag ) {
		this.serializableList.forEach( serializable->serializable.write( tag ) );
	}

	@Override
	public void read( CompoundTag tag ) {
		this.serializableList.forEach( serializable->serializable.read( tag ) );
	}

	public void onServer( ServerPlayer sender, NetworkEvent.Context context ) {}

	@OnlyIn( Dist.CLIENT )
	public void onClient( NetworkEvent.Context context ) {}

	protected Data< Boolean > addBoolean() {
		return this.add( DataBoolean::new );
	}

	protected Data< BlockPos > addBlockPos() {
		return this.add( DataBlockPos::new );
	}

	protected Data< Enchantment > addEnchantment() {
		return this.add( DataEnchantment::new );
	}

	protected Data< EntityType< ? > > addEntityType() {
		return this.add( DataEntityType::new );
	}

	protected Data< Float > addFloat() {
		return this.add( DataFloat::new );
	}

	protected Data< Integer > addInteger() {
		return this.add( DataInteger::new );
	}

	protected < SerializableType extends ISerializable > Data< List< SerializableType > > addList( Supplier< SerializableType > supplier ) {
		return this.add( ()->new DataList<>( supplier ) );
	}

	protected Data< ResourceLocation > addResourceLocation() {
		return this.add( DataResourceLocation::new );
	}

	protected Data< String > addString() {
		return this.add( DataString::new );
	}

	protected < StructureType extends SerializableStructure > Data< StructureType > addStructure( Supplier< StructureType > supplier ) {
		return this.add( ()->new DataStructure<>( supplier ) );
	}

	private < SerializableType extends ISerializable > SerializableType add( Supplier< SerializableType > supplier ) {
		SerializableType serializable = supplier.get();
		this.serializableList.add( serializable );

		return serializable;
	}
}
