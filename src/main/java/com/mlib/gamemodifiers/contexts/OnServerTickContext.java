package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.Context;
import com.mlib.gamemodifiers.data.OnServerTickData;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class OnServerTickContext extends Context< OnServerTickData > {
	static final List< OnServerTickContext > CONTEXTS = new ArrayList<>();

	public OnServerTickContext( Consumer< OnServerTickData > consumer, String configName, String configComment ) {
		super( OnServerTickData.class, consumer, configName, configComment );
		Context.addSorted( CONTEXTS, this );
	}

	public OnServerTickContext( Consumer< OnServerTickData > consumer ) {
		this( consumer, "OnServerTick", "" );
	}

	@SubscribeEvent
	public static void onServerTick( TickEvent.ServerTickEvent event ) {
		Context.accept( CONTEXTS, new OnServerTickData( event ) );
	}
}
