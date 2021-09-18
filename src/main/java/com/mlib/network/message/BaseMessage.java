package com.mlib.network.message;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.function.Supplier;

/** Class for easier sending basic value messages from client to server. */
public abstract class BaseMessage {
	public void handle( Supplier< NetworkEvent.Context > contextSupplier ) {
		NetworkEvent.Context context = contextSupplier.get();
		context.enqueueWork( ()->{
			ServerPlayer sender = context.getSender();
			if( sender != null )
				receiveMessage( sender, context );
		} );
		context.setPacketHandled( true );
	}

	public abstract void receiveMessage( ServerPlayer sender, NetworkEvent.Context context );
}
