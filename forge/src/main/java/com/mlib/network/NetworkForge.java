package com.mlib.network;

import com.mlib.data.ISerializable;
import com.mlib.data.SerializableHelper;
import com.mlib.modhelper.DataForge;
import com.mlib.modhelper.ModHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;

import java.util.List;

public class NetworkForge implements INetworkPlatform {
	final String protocolVersion = "1";

	@Override
	public void register( ModHelper helper, List< NetworkObject< ? > > objects ) {
		FMLJavaModLoadingContext.get().getModEventBus().addListener( ( final FMLCommonSetupEvent event )->{
			DataForge data = helper.getData( DataForge.class );
			data.channel = NetworkRegistry.newSimpleChannel( helper.getLocation( "main" ), ()->this.protocolVersion, this.protocolVersion::equals, this.protocolVersion::equals );
			objects.forEach( this::register );
		} );
	}

	@Override
	public < Type extends ISerializable > void sendToServer( NetworkObject< Type > object, Type message ) {
		DataForge data = object.networkHandler.helper.getData( DataForge.class );
		data.channel.send( PacketDistributor.SERVER.with( ()->null ), message );
	}

	@Override
	public < Type extends ISerializable > void sendToClients( NetworkObject< Type > object, Type message, List< ServerPlayer > players ) {
		DataForge data = object.networkHandler.helper.getData( DataForge.class );
		players.forEach( player->data.channel.send( PacketDistributor.PLAYER.with( ()->player ), message ) );
	}

	private < Type extends ISerializable > void register( NetworkObject< Type > object ) {
		DataForge data = object.networkHandler.helper.getData( DataForge.class );
		data.channel.registerMessage(
			data.messageIdx++,
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
