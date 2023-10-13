package com.mlib.network;

import com.mlib.data.ISerializable;
import com.mlib.platform.Side;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;
import java.util.function.Supplier;

public class NetworkObject< Type extends ISerializable > {
	final NetworkHandler networkHandler;
	final ResourceLocation id;
	final Class< Type > clazz;
	final Supplier< Type > instance;

	public NetworkObject( NetworkHandler networkHandler, ResourceLocation id, Class< Type > clazz, Supplier< Type > instance ) {
		this.networkHandler = networkHandler;
		this.id = id;
		this.clazz = clazz;
		this.instance = instance;
	}

	public void sendToClients( Type message ) {
		NetworkHandler.PLATFORM.sendToClients( this, message, Side.getServer().getPlayerList().getPlayers() );
	}

	public void sendToClients( List< ServerPlayer > players, Type message ) {
		NetworkHandler.PLATFORM.sendToClients( this, message, players );
	}

	public void sendToServer( Type message ) {
		NetworkHandler.PLATFORM.sendToServer( this, message );
	}
}
