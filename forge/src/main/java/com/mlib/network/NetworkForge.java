package com.mlib.network;

import com.mlib.data.ISerializable;
import com.mlib.data.SerializableHelper;
import com.mlib.modhelper.DataForge;
import com.mlib.modhelper.ModHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.PacketDistributor;

import java.util.List;

public class NetworkForge implements INetworkPlatform {
	final int protocolVersion = 1;

	@Override
	public void register( ModHelper helper, List< NetworkObject< ? > > objects ) {
		FMLJavaModLoadingContext.get().getModEventBus().addListener( ( final FMLCommonSetupEvent event )->{
			DataForge data = helper.getData( DataForge.class );
			data.channel = ChannelBuilder.named( helper.getLocation( "main" ) )
				.networkProtocolVersion( this.protocolVersion )
				.acceptedVersions( ( status, version )->version == this.protocolVersion )
				.simpleChannel();
			objects.forEach( this::register );
		} );
	}

	@Override
	public < Type extends ISerializable > void sendToServer( NetworkObject< Type > object, Type message ) {
		DataForge data = object.networkHandler.helper.getData( DataForge.class );
		data.channel.send( message, PacketDistributor.SERVER.with( null ) );
	}

	@Override
	public < Type extends ISerializable > void sendToClients( NetworkObject< Type > object, Type message, List< ServerPlayer > players ) {
		DataForge data = object.networkHandler.helper.getData( DataForge.class );
		players.forEach( player->data.channel.send( message, PacketDistributor.PLAYER.with( player ) ) );
	}

	private < Type extends ISerializable > void register( NetworkObject< Type > object ) {
		DataForge data = object.networkHandler.helper.getData( DataForge.class );
		data.channel.messageBuilder( object.clazz )
			.encoder( ISerializable::write )
			.decoder( buffer->SerializableHelper.read( object.instance, buffer ) )
			.consumerNetworkThread( ( serializable, context )->{
				context.enqueueWork( ()->{
					ServerPlayer sender = context.getSender();
					if( sender != null ) {
						serializable.onServer( sender );
					} else {
						serializable.onClient();
					}
				} );
				context.setPacketHandled( true );
			} )
			.add();;
	}
}
