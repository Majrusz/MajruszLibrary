package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.Context;
import com.mlib.gamemodifiers.data.OnPlayerLoggedData;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class OnPlayerLoggedContext extends Context< OnPlayerLoggedData > {
	static final List< OnPlayerLoggedContext > CONTEXTS = new ArrayList<>();

	public OnPlayerLoggedContext( Consumer< OnPlayerLoggedData > consumer, String configName, String configComment ) {
		super( consumer, configName, configComment );
		CONTEXTS.add( this );
	}

	public OnPlayerLoggedContext( Consumer< OnPlayerLoggedData > consumer ) {
		this( consumer, "OnPlayerLogged", "" );
	}

	@SubscribeEvent public static void onPlayerLogged( PlayerEvent.PlayerLoggedInEvent event ) {
		handleContexts( new OnPlayerLoggedData( event ), CONTEXTS );
	}
}
