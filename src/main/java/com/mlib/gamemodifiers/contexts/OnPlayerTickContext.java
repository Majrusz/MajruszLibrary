package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.Context;
import com.mlib.gamemodifiers.data.OnPlayerTickData;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class OnPlayerTickContext extends Context< OnPlayerTickData > {
	static final List< OnPlayerTickContext > CONTEXTS = new ArrayList<>();

	public OnPlayerTickContext( Consumer< OnPlayerTickData > consumer, String configName, String configComment ) {
		super( OnPlayerTickData.class, consumer, configName, configComment );
		CONTEXTS.add( this );
	}

	public OnPlayerTickContext( Consumer< OnPlayerTickData > consumer ) {
		this( consumer, "OnPlayerTick", "" );
	}

	@SubscribeEvent
	public static void onPlayerTick( TickEvent.PlayerTickEvent event ) {
		handleContexts( new OnPlayerTickData( event ), CONTEXTS );
	}
}
