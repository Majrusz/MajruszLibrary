package com.mlib.network.message;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;

/** Class for easier sending single float and entity. */
public class EntityFloatMessage extends BaseMessage {
	public final float value;
	public final int id;

	public EntityFloatMessage( float value, int id ) {
		this.value = value;
		this.id = id;
	}

	public EntityFloatMessage( Entity entity, float value ) {
		this( value, entity.getId() );
	}

	public EntityFloatMessage( FriendlyByteBuf buffer ) {
		this( buffer.readFloat(), buffer.readVarInt() );
	}

	public void encode( FriendlyByteBuf buffer ) {
		buffer.writeFloat( this.value );
		buffer.writeVarInt( this.id );
	}
}
