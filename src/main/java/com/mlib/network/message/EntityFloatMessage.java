package com.mlib.network.message;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;

/** Class for easier sending single float and entity. */
public class EntityFloatMessage extends EntityMessage {
	public final float value;

	public EntityFloatMessage( float value, int id ) {
		super( id );
		this.value = value;
	}

	public EntityFloatMessage( Entity entity, float value ) {
		this( value, entity.getId() );
	}

	public EntityFloatMessage( FriendlyByteBuf buffer ) {
		this( buffer.readFloat(), buffer.readVarInt() );
	}

	public void encode( FriendlyByteBuf buffer ) {
		super.encode( buffer );
		buffer.writeFloat( this.value );
	}
}
