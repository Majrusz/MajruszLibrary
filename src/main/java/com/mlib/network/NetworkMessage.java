package com.mlib.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Deprecated( since = "3.2.0 (use SerializableStructure instead)", forRemoval = true )
public class NetworkMessage {
	protected final List< Consumer< FriendlyByteBuf > > encoders = new ArrayList<>();

	public void handle( Supplier< NetworkEvent.Context > contextSupplier ) {
		NetworkEvent.Context context = contextSupplier.get();
		context.enqueueWork( ()->{
			ServerPlayer sender = context.getSender();
			if( sender != null ) {
				receiveMessage( sender, context );
			} else {
				DistExecutor.unsafeRunWhenOn( Dist.CLIENT, ()->()->receiveMessage( context ) );
			}
		} );
		context.setPacketHandled( true );
	}

	public void receiveMessage( ServerPlayer sender, NetworkEvent.Context context ) {}

	@OnlyIn( Dist.CLIENT )
	public void receiveMessage( NetworkEvent.Context context ) {}

	public void encode( FriendlyByteBuf buffer ) {
		this.encoders.forEach( encoder->encoder.accept( buffer ) );
	}

	protected boolean write( boolean value ) {
		this.encoders.add( buffer->buffer.writeBoolean( value ) );

		return value;
	}

	protected boolean readBoolean( FriendlyByteBuf buffer ) {
		return buffer.readBoolean();
	}

	protected float write( float value ) {
		this.encoders.add( buffer->buffer.writeFloat( value ) );

		return value;
	}

	protected float readFloat( FriendlyByteBuf buffer ) {
		return buffer.readFloat();
	}

	protected int write( int value ) {
		this.encoders.add( buffer->buffer.writeInt( value ) );

		return value;
	}

	protected int readInt( FriendlyByteBuf buffer ) {
		return buffer.readInt();
	}

	protected int write( Entity entity ) {
		this.encoders.add( buffer->buffer.writeVarInt( entity.getId() ) );

		return entity.getId();
	}

	protected int readEntity( FriendlyByteBuf buffer ) {
		return buffer.readVarInt();
	}

	protected < Type extends Enum< Type > > Type write( Type value ) {
		this.encoders.add( buffer->buffer.writeEnum( value ) );

		return value;
	}

	protected < Type extends Enum< Type > > Type readEnum( FriendlyByteBuf buffer, Class< Type > enumClass ) {
		return buffer.readEnum( enumClass );
	}

	protected String write( String text ) {
		this.encoders.add( buffer->buffer.writeUtf( text ) );

		return text;
	}

	protected String readString( FriendlyByteBuf buffer ) {
		return buffer.readUtf();
	}

	protected < Type extends NetworkMessage > List< Type > write( List< Type > list ) {
		this.encoders.add( buffer->buffer.writeCollection( list, ( byteBuffer, element )->element.encode( byteBuffer ) ) );

		return list;
	}

	protected < Type extends NetworkMessage > List< Type > readList( FriendlyByteBuf buffer,
		Function< FriendlyByteBuf, Type > supplier
	) {
		return buffer.readList( supplier::apply );
	}
}
