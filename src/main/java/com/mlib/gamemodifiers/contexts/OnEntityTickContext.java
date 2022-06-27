package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.Context;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber
public class OnEntityTickContext extends Context {
	static final List< OnEntityTickContext > CONTEXTS = new ArrayList<>();

	public OnEntityTickContext( String configName, String configComment ) {
		super( configName, configComment );
		CONTEXTS.add( this );
	}

	public OnEntityTickContext() {
		this( "OnEntityTick", "" );
	}

	@SubscribeEvent
	public static void onEntityTick( LivingEvent.LivingUpdateEvent event ) {
		handleContexts( context->new Data( context, event ), CONTEXTS );
	}

	public static class Data extends Context.Data {
		public final LivingEvent.LivingUpdateEvent event;

		Data( Context context, LivingEvent.LivingUpdateEvent event ) {
			super( context, null );
			this.event = event;
		}
	}
}
