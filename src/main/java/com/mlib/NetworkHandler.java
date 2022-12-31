package com.mlib;

import com.mlib.mixininterfaces.IMixinEntity;
import com.mlib.network.NetworkMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class NetworkHandler {
	public static SimpleChannel CHANNEL;
	static final String PROTOCOL_VERSION = "1";

	static void register( final FMLCommonSetupEvent event ) {
		CHANNEL = NetworkRegistry.newSimpleChannel( Registries.getLocation( "main" ), ()->PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals );
		CHANNEL.registerMessage( 0, GlowEntityMessage.class, GlowEntityMessage::encode, GlowEntityMessage::new, GlowEntityMessage::handle );
	}

	public static class GlowEntityMessage extends NetworkMessage {
		final int entityId;
		final int ticks;

		public GlowEntityMessage( Entity entity, int ticks ) {
			this.entityId = this.write( entity );
			this.ticks = this.write( ticks );
		}

		public GlowEntityMessage( FriendlyByteBuf buffer ) {
			this.entityId = this.readEntity( buffer );
			this.ticks = this.readInt( buffer );
		}

		@Override
		@OnlyIn( Dist.CLIENT )
		public void receiveMessage( NetworkEvent.Context context ) {
			Level level = Minecraft.getInstance().level;
			if( level == null )
				return;

			IMixinEntity.addGlowTicks( level.getEntity( this.entityId ), this.ticks );
		}
	}
}
