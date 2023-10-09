package com.mlib.network;

import com.mlib.MajruszLibrary;
import com.mlib.data.ISerializable;
import com.mlib.data.SerializableHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.List;

public class NetworkForge implements INetworkPlatform {
	final String protocolVersion = "1";
	SimpleChannel channel = null;
	int idx = 0;

	@Override
	public void register( List< NetworkObject< ? > > objects ) {
		FMLJavaModLoadingContext.get().getModEventBus().addListener( ( final FMLCommonSetupEvent event )->{
			this.channel = NetworkRegistry.newSimpleChannel( MajruszLibrary.HELPER.getLocation( "main" ), ()->this.protocolVersion, this.protocolVersion::equals, this.protocolVersion::equals );
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
			( serializable, contextSupplier )->{
				NetworkEvent.Context context = contextSupplier.get();
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
