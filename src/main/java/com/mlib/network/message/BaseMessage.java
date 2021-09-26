package com.mlib.network.message;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.function.Supplier;

/** Class for easier sending basic value messages from client to server. */
public class BaseMessage {
	public void handle( Supplier< NetworkEvent.Context > contextSupplier ) {
		NetworkEvent.Context context = contextSupplier.get();
		context.enqueueWork( ()->{
			ServerPlayer sender = context.getSender();
			if( sender != null ) {
				receiveMessage( sender, context );
			} else {
				context.enqueueWork( ()->DistExecutor.unsafeRunWhenOn( Dist.CLIENT, ()->()->receiveMessage( context ) ) );
			}
		} );
		context.setPacketHandled( true );
	}

	public void receiveMessage( ServerPlayer sender, NetworkEvent.Context context ) {}

	public void receiveMessage( NetworkEvent.Context context ) {}
}
