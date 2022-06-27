package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.Context;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber
public class OnPlayerTickContext extends Context {
	static final List< OnPlayerTickContext > CONTEXTS = new ArrayList<>();

	public OnPlayerTickContext( String configName, String configComment ) {
		super( configName, configComment );
		CONTEXTS.add( this );
	}

	public OnPlayerTickContext() {
		this( "OnPlayerTick", "" );
	}

	@SubscribeEvent
	public static void onPlayerTick( TickEvent.PlayerTickEvent event ) {
		handleContexts( context->new Data( context, event ), CONTEXTS );
	}

	public static class Data extends Context.Data {
		public final TickEvent.PlayerTickEvent event;
		public final Player player;

		Data( Context context, TickEvent.PlayerTickEvent event ) {
			super( context, null );
			this.event = event;
			this.player = event.player;
		}
	}
}
