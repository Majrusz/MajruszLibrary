package com.mlib;

import com.mlib.data.Data;
import com.mlib.data.SerializableStructure;
import com.mlib.mixininterfaces.IMixinEntity;
import net.minecraft.client.Minecraft;
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
		SerializableStructure.register( CHANNEL, 0, EntityGlow.class, EntityGlow::new );
	}

	public static class EntityGlow extends SerializableStructure {
		final Data< Integer > entityId = this.addInteger();
		final Data< Integer > ticks = this.addInteger();

		public EntityGlow( Entity entity, int ticks ) {
			this.entityId.set( entity.getId() );
			this.ticks.set( ticks );
		}

		public EntityGlow() {}

		@Override
		@OnlyIn( Dist.CLIENT )
		public void onClient( NetworkEvent.Context context ) {
			Level level = Minecraft.getInstance().level;
			if( level == null )
				return;

			IMixinEntity.addGlowTicks( level.getEntity( this.entityId.get() ), this.ticks.get() );
		}
	}
}
