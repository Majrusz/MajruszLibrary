package com.mlib.modhelper;

import com.mlib.data.ISerializable;
import com.mlib.data.SerializableHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

class NetworkHandler {
	final String protocolVersion = "1";
	final ResourceLocation id;
	final List< Consumer< SimpleChannel > > pendingConsumers = new ArrayList<>();
	SimpleChannel channel = null;

	public NetworkHandler( ModHelper helper ) {
		this.id = helper.getLocation( "main" );

		helper.onRegister( ()->helper.getEventBus().addListener( this::register ) );
	}

	public < Type extends ISerializable > void add( Class< Type > clazz, Supplier< Type > supplier ) {
		int idx = this.pendingConsumers.size();
		this.pendingConsumers.add( channel->channel.registerMessage(
			idx,
			clazz,
			ISerializable::write,
			( buffer )->SerializableHelper.read( supplier, buffer ),
			( serializable, contextSupplier )->{
				NetworkEvent.Context context = contextSupplier.get();
				context.enqueueWork( ()->{
					ServerPlayer sender = context.getSender();
					if( sender != null ) {
						serializable.onServer( sender, context );
					} else {
						DistExecutor.unsafeRunWhenOn( Dist.CLIENT, ()->()->serializable.onClient( context ) );
					}
				} );
				context.setPacketHandled( true );
			}
		) );
	}

	public < Type > void send( PacketDistributor.PacketTarget target, Type message ) {
		this.channel.send( target, message );
	}

	private void register( final FMLCommonSetupEvent event ) {
		if( this.pendingConsumers.isEmpty() ) {
			return;
		}

		this.channel = NetworkRegistry.newSimpleChannel( this.id, ()->this.protocolVersion, this.protocolVersion::equals, this.protocolVersion::equals );
		this.pendingConsumers.forEach( consumer->consumer.accept( this.channel ) );
	}
}