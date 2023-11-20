package com.mlib.network;

import com.mlib.platform.Side;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class NetworkObject< Type > {
	final NetworkHandler networkHandler;
	final ResourceLocation id;
	final Class< Type > clazz;
	final Supplier< Type > instance;
	final List< Consumer< Type > > clientCallbacks = new ArrayList<>();
	final List< BiConsumer< Type, ServerPlayer > > serverCallbacks = new ArrayList<>();

	public NetworkObject( NetworkHandler networkHandler, ResourceLocation id, Class< Type > clazz, Supplier< Type > instance ) {
		this.networkHandler = networkHandler;
		this.id = id;
		this.clazz = clazz;
		this.instance = instance;
	}

	public void sendToClients( List< ServerPlayer > players, Type message ) {
		NetworkHandler.PLATFORM.sendToClients( this, message, players );
	}

	public void sendToClients( List< ServerPlayer > players ) {
		this.sendToClients( players, this.instance.get() );
	}

	public void sendToClients( Type message ) {
		this.sendToClients( Side.getServer().getPlayerList().getPlayers(), message );
	}

	public void sendToClients() {
		this.sendToClients( this.instance.get() );
	}

	public void sendToClient( ServerPlayer player, Type message ) {
		this.sendToClients( List.of( player ), message );
	}

	public void sendToClient( ServerPlayer player ) {
		this.sendToClient( player, this.instance.get() );
	}

	public void sendToServer( Type message ) {
		NetworkHandler.PLATFORM.sendToServer( this, message );
	}

	public void sendToServer() {
		this.sendToServer( this.instance.get() );
	}

	public void broadcastOnClient( Type message ) {
		this.clientCallbacks.forEach( consumer->consumer.accept( message ) );
	}

	public void broadcastOnServer( Type message, ServerPlayer player ) {
		this.serverCallbacks.forEach( consumer->consumer.accept( message, player ) );
	}

	public void addClientCallback( Consumer< Type > callback ) {
		this.clientCallbacks.add( callback );
	}

	public void addClientCallback( Runnable callback ) {
		this.addClientCallback( data->callback.run() );
	}

	public void addServerCallback( BiConsumer< Type, ServerPlayer > callback ) {
		this.serverCallbacks.add( callback );
	}

	public void addServerCallback( Consumer< ServerPlayer > callback ) {
		this.addServerCallback( ( data, player )->callback.accept( player ) );
	}
}
