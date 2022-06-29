package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.Context;
import com.mlib.gamemodifiers.data.OnDimensionChangedData;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class OnDimensionChangedContext extends Context< OnDimensionChangedData > {
	static final List< OnDimensionChangedContext > CONTEXTS = new ArrayList<>();

	public OnDimensionChangedContext( Consumer< OnDimensionChangedData > consumer, String configName, String configComment ) {
		super( consumer, configName, configComment );
		CONTEXTS.add( this );
	}

	public OnDimensionChangedContext( Consumer< OnDimensionChangedData > consumer ) {
		this( consumer, "OnDimensionChanged", "" );
	}

	@SubscribeEvent public static void onDimensionChanged( PlayerEvent.PlayerChangedDimensionEvent event ) {
		handleContexts( new OnDimensionChangedData( event ), CONTEXTS );
	}
}
