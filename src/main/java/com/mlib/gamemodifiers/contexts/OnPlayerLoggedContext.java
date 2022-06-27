package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.Context;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber
public class OnPlayerLoggedContext extends Context {
	static final List< OnPlayerLoggedContext > CONTEXTS = new ArrayList<>();

	public OnPlayerLoggedContext( String configName, String configComment ) {
		super( configName, configComment );
		CONTEXTS.add( this );
	}

	public OnPlayerLoggedContext() {
		this( "OnPlayerLogged", "" );
	}

	@SubscribeEvent
	public static void onPlayerLogged( PlayerEvent.PlayerLoggedInEvent event ) {
		handleContexts( context->new Data( context, event ), CONTEXTS );
	}

	public static class Data extends Context.Data {
		public final PlayerEvent.PlayerLoggedInEvent event;
		public final Player player;

		Data( Context context, PlayerEvent.PlayerLoggedInEvent event ) {
			super( context, event.getEntityLiving() );
			this.event = event;
			this.player = event.getPlayer();
		}
	}
}
