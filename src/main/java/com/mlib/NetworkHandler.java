package com.mlib;

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
		int entityId = 0;
		int ticks = 0;

		public EntityGlow( Entity entity, int ticks ) {
			this();

			this.entityId = entity.getId();
			this.ticks = ticks;
		}

		public EntityGlow() {
			this.define( null, ()->this.entityId, x->this.entityId = x );
			this.define( null, ()->this.ticks, x->this.ticks = x );
		}

		@Override
		@OnlyIn( Dist.CLIENT )
		public void onClient( NetworkEvent.Context context ) {
			Level level = Minecraft.getInstance().level;
			if( level == null )
				return;

			IMixinEntity.addGlowTicks( level.getEntity( this.entityId ), this.ticks );
		}
	}
}
