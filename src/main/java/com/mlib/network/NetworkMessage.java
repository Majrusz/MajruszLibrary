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
import java.util.function.Supplier;

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

	protected float write( float value ) {
		this.encoders.add( buffer->buffer.writeFloat( value ) );

		return value;
	}

	protected float write( int value ) {
		this.encoders.add( buffer->buffer.writeVarInt( value ) );

		return value;
	}

	protected int write( Entity entity ) {
		this.encoders.add( buffer->buffer.writeVarInt( entity.getId() ) );

		return entity.getId();
	}
}
