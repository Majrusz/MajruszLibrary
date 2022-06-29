package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.Context;
import com.mlib.gamemodifiers.data.OnItemFishedData;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class OnItemFishedContext extends Context< OnItemFishedData > {
	static final List< OnItemFishedContext > CONTEXTS = new ArrayList<>();

	public OnItemFishedContext( Consumer< OnItemFishedData > consumer, String configName, String configComment ) {
		super( consumer, configName, configComment );
		CONTEXTS.add( this );
	}

	public OnItemFishedContext( Consumer< OnItemFishedData > consumer ) {
		this( consumer, "OnItemFished", "" );
	}

	@SubscribeEvent public static void onDimensionChanged( ItemFishedEvent event ) {
		handleContexts( new OnItemFishedData( event ), CONTEXTS );
	}
}
