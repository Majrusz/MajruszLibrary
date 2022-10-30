package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.ContextBase;
import com.mlib.gamemodifiers.data.OnServerTickData;
import com.mlib.gamemodifiers.parameters.ContextParameters;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

@Deprecated
@Mod.EventBusSubscriber
public class OnServerTickContext extends ContextBase< OnServerTickData > {
	static final List< OnServerTickContext > CONTEXTS = Collections.synchronizedList( new ArrayList<>() );

	public OnServerTickContext( Consumer< OnServerTickData > consumer, ContextParameters params ) {
		super( OnServerTickData.class, consumer, params );
		ContextBase.addSorted( CONTEXTS, this );
	}

	public OnServerTickContext( Consumer< OnServerTickData > consumer ) {
		this( consumer, new ContextParameters() );
	}

	@SubscribeEvent
	public static void onServerTick( TickEvent.ServerTickEvent event ) {
		ContextBase.accept( CONTEXTS, new OnServerTickData( event ) );
	}
}
