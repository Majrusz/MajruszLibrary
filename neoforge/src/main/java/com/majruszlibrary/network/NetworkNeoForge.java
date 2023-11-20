package com.majruszlibrary.network;

import com.majruszlibrary.data.Serializables;
import com.majruszlibrary.modhelper.ModHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.List;

public class NetworkNeoForge implements INetworkPlatform {
	final String protocolVersion = "1";
	SimpleChannel channel = null;
	int idx = 0;

	@Override
	public void register( ModHelper helper, List< NetworkObject< ? > > objects ) {
		FMLJavaModLoadingContext.get().getModEventBus().addListener( ( final FMLCommonSetupEvent event )->{
			this.channel = NetworkRegistry.newSimpleChannel( helper.getLocation( "main" ), ()->this.protocolVersion, this.protocolVersion::equals, this.protocolVersion::equals );
			objects.forEach( this::register );
		} );
	}

	@Override
	public < Type > void sendToServer( NetworkObject< Type > object, Type message ) {
		this.channel.send( PacketDistributor.SERVER.with( ()->null ), message );
	}

	@Override
	public < Type > void sendToClients( NetworkObject< Type > object, Type message, List< ServerPlayer > players ) {
		players.forEach( player->this.channel.send( PacketDistributor.PLAYER.with( ()->player ), message ) );
	}

	private < Type > void register( NetworkObject< Type > object ) {
		this.channel.registerMessage(
			this.idx++,
			object.clazz,
			Serializables::write,
			buffer->Serializables.read( object.instance.get(), buffer ),
			( message, contextSupplier )->{
				NetworkEvent.Context context = contextSupplier.get();
				context.enqueueWork( ()->{
					ServerPlayer sender = context.getSender();
					if( sender != null ) {
						object.broadcastOnServer( message, sender );
					} else {
						object.broadcastOnClient( message );
					}
				} );
				context.setPacketHandled( true );
			}
		);
	}
}
