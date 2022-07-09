package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.Context;
import com.mlib.gamemodifiers.data.OnPlayerTickData;
import com.mlib.gamemodifiers.parameters.ContextParameters;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class OnPlayerTickContext extends Context< OnPlayerTickData > {
	static final List< OnPlayerTickContext > CONTEXTS = new ArrayList<>();

	public OnPlayerTickContext( Consumer< OnPlayerTickData > consumer, ContextParameters params ) {
		super( OnPlayerTickData.class, consumer, params );
		Context.addSorted( CONTEXTS, this );
	}

	public OnPlayerTickContext( Consumer< OnPlayerTickData > consumer ) {
		this( consumer, new ContextParameters() );
	}

	@SubscribeEvent
	public static void onPlayerTick( TickEvent.PlayerTickEvent event ) {
		Context.accept( CONTEXTS, new OnPlayerTickData( event ) );
	}
}
