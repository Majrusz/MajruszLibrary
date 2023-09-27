package net.mlib.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.mlib.MajruszLibrary;
import net.mlib.data.ISerializable;

import java.util.List;
import java.util.function.Supplier;

public class NetworkObject< Type extends ISerializable > {
	final NetworkHandler networkHandler;
	final ResourceLocation id;
	final Class< Type > clazz;
	final Supplier< Type > newInstance;

	public NetworkObject( NetworkHandler networkHandler, ResourceLocation id, Class< Type > clazz ) {
		this.networkHandler = networkHandler;
		this.id = id;
		this.clazz = clazz;
		this.newInstance = ()->{
			try {
				return clazz.getConstructor().newInstance();
			} catch( Exception exception ) {
				throw new IllegalArgumentException();
			}
		};
	}

	public void sendToClients( Type message ) {
		NetworkHandler.PLATFORM.sendToClients( this, message, MajruszLibrary.SIDE.getServer().getPlayerList().getPlayers() );
	}

	public void sendToClients( List< ServerPlayer > players, Type message ) {
		NetworkHandler.PLATFORM.sendToClients( this, message, players );
	}

	public void sendToServer( Type message ) {
		NetworkHandler.PLATFORM.sendToServer( this, message );
	}
}
