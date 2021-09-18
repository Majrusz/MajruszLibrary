package com.mlib.network.message;

import net.minecraft.network.FriendlyByteBuf;

/** Class for easier sending single float from client to the server. */
public abstract class FloatMessage extends BaseMessage {
	protected float value;

	public FloatMessage( float value ) {
		this.value = value;
	}

	public FloatMessage( FriendlyByteBuf buffer ) {
		this( buffer.readFloat() );
	}

	public void encode( FriendlyByteBuf buffer ) {
		buffer.writeFloat( this.value );
	}
}
