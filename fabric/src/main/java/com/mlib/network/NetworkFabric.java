package com.mlib.network;

import com.mlib.data.Serializables;
import com.mlib.modhelper.ModHelper;
import com.mlib.platform.Side;
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
	public void register( ModHelper helper, List< NetworkObject< ? > > objects ) {
		objects.forEach( this::registerOnServer );
		Side.runOnClient( ()->()->objects.forEach( this::registerOnClient ) );
	}

	@Override
	public < Type > void sendToServer( NetworkObject< Type > object, Type message ) {
		ClientPlayNetworking.send( object.id, Serializables.write( message, PacketByteBufs.create() ) );
	}

	@Override
	public < Type > void sendToClients( NetworkObject< Type > object, Type message, List< ServerPlayer > players ) {
		players.forEach( player->{
			ServerPlayNetworking.send( player, object.id, Serializables.write( message, PacketByteBufs.create() ) );
		} );
	}

	private < Type > void registerOnServer( NetworkObject< Type > object ) {
		ServerPlayNetworking.registerGlobalReceiver( object.id, ( MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf buffer, PacketSender responseSender )->{
			object.broadcastOnServer( Serializables.read( object.instance.get(), buffer ), player );
		} );
	}

	@Environment( EnvType.CLIENT )
	private < Type > void registerOnClient( NetworkObject< Type > object ) {
		ClientPlayNetworking.registerGlobalReceiver( object.id, ( Minecraft client, ClientPacketListener handler, FriendlyByteBuf buffer, PacketSender responseSender )->{
			object.broadcastOnClient( Serializables.read( object.instance.get(), buffer ) );
		} );
	}
}
