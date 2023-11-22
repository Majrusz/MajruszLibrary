package com.majruszlibrary.network;

import com.majruszlibrary.data.Serializables;
import com.majruszlibrary.modhelper.DataNeoForge;
import com.majruszlibrary.modhelper.ModHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;

import java.util.List;

public class NetworkNeoForge implements INetworkPlatform {
	final String protocolVersion = "1";

	@Override
	public void register( ModHelper helper, List< NetworkObject< ? > > objects ) {
		FMLJavaModLoadingContext.get().getModEventBus().addListener( ( final FMLCommonSetupEvent event )->{
			DataNeoForge data = helper.getData( DataNeoForge.class );
			data.channel = NetworkRegistry.newSimpleChannel( helper.getLocation( "main" ), ()->this.protocolVersion, this.protocolVersion::equals, this.protocolVersion::equals );
			objects.forEach( this::register );
		} );
	}

	@Override
	public < Type > void sendToServer( NetworkObject< Type > object, Type message ) {
		DataNeoForge data = object.networkHandler.helper.getData( DataNeoForge.class );
		data.channel.send( PacketDistributor.SERVER.with( ()->null ), message );
	}

	@Override
	public < Type > void sendToClients( NetworkObject< Type > object, Type message, List< ServerPlayer > players ) {
		DataNeoForge data = object.networkHandler.helper.getData( DataNeoForge.class );
		players.forEach( player->data.channel.send( PacketDistributor.PLAYER.with( ()->player ), message ) );
	}

	private < Type > void register( NetworkObject< Type > object ) {
		DataNeoForge data = object.networkHandler.helper.getData( DataNeoForge.class );
		data.channel.registerMessage(
			data.messageIdx++,
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
