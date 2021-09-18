package com.mlib.network.message;

import net.minecraft.network.FriendlyByteBuf;

/** Class for easier sending single boolean from client to the server. */
public abstract class BooleanMessage extends BaseMessage {
	protected boolean value;

	public BooleanMessage( boolean value ) {
		this.value = value;
	}

	public BooleanMessage( FriendlyByteBuf buffer ) {
		this( buffer.readBoolean() );
	}

	public void encode( FriendlyByteBuf buffer ) {
		buffer.writeBoolean( this.value );
	}
}
