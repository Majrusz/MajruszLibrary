package com.mlib.network.message;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;

/** Class for easier sending entity id. */
public class EntityMessage extends BaseMessage {
	public final int id;

	public EntityMessage( int id ) {
		this.id = id;
	}

	public EntityMessage( Entity entity ) {
		this( entity.getId() );
	}

	public EntityMessage( FriendlyByteBuf buffer ) {
		this( buffer.readVarInt() );
	}

	public void encode( FriendlyByteBuf buffer ) {
		buffer.writeVarInt( this.id );
	}
}
