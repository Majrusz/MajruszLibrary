package com.mlib.network;

import com.mlib.data.ISerializable;
import com.mlib.data.SerializableHelper;
import com.mlib.platform.Platform;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

import java.util.List;

public class NetworkFabric implements INetworkPlatform {
	@Override
	public void register( List< NetworkObject< ? > > objects ) {
		this.registerOnServer( objects );
		Platform.runOnClient( ()->()->this.registerOnClient( objects ) );
	}

	@Override
	public < Type extends ISerializable > void sendToServer( NetworkObject< Type > object, Type message ) {
		ClientPlayNetworking.send( object.id, SerializableHelper.write( ()->message, PacketByteBufs.create() ) );
	}

	@Override
	public < Type extends ISerializable > void sendToClients( NetworkObject< Type > object, Type message, List< ServerPlayer > players ) {
		players.forEach( player->{
			ServerPlayNetworking.send( player, object.id, SerializableHelper.write( ()->message, PacketByteBufs.create() ) );
		} );
	}

	private void registerOnServer( List< NetworkObject< ? > > objects ) {
		objects.forEach( object->{
			ServerPlayNetworking.registerGlobalReceiver( object.id, ( MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf buffer, PacketSender responseSender )->{
				ISerializable serializable = SerializableHelper.read( object.instance, buffer );
				server.execute( ()->serializable.onServer( player ) );
			} );
		} );
	}

	@Environment( EnvType.CLIENT )
	private void registerOnClient( List< NetworkObject< ? > > objects ) {
		objects.forEach( object->{
			ClientPlayNetworking.registerGlobalReceiver( object.id, ( Minecraft client, ClientPacketListener handler, FriendlyByteBuf buffer, PacketSender responseSender )->{
				ISerializable serializable = SerializableHelper.read( object.instance, buffer );
				client.execute( serializable::onClient );
			} );
		} );
	}
}
