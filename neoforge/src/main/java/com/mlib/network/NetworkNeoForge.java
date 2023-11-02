package com.mlib.network;

import com.mlib.data.ISerializable;
import com.mlib.data.SerializableHelper;
import com.mlib.modhelper.ModHelper;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.neoforge.network.NetworkRegistry;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.simple.SimpleChannel;

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
	public < Type extends ISerializable > void sendToServer( NetworkObject< Type > object, Type message ) {
		this.channel.send( PacketDistributor.SERVER.with( ()->null ), message );
	}

	@Override
	public < Type extends ISerializable > void sendToClients( NetworkObject< Type > object, Type message, List< ServerPlayer > players ) {
		players.forEach( player->this.channel.send( PacketDistributor.PLAYER.with( ()->player ), message ) );
	}

	private < Type extends ISerializable > void register( NetworkObject< Type > object ) {
		this.channel.registerMessage(
			this.idx++,
			object.clazz,
			ISerializable::write,
			( buffer )->SerializableHelper.read( object.instance, buffer ),
			( serializable, context )->{
				context.enqueueWork( ()->{
					ServerPlayer sender = context.getSender();
					if( sender != null ) {
						serializable.onServer( sender );
					} else {
						serializable.onClient();
					}
				} );
				context.setPacketHandled( true );
			}
		);
	}
}
