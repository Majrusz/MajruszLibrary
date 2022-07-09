package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.Context;
import com.mlib.gamemodifiers.data.OnBreakSpeedData;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class OnBreakSpeedContext extends Context< OnBreakSpeedData > {
	static final List< OnBreakSpeedContext > CONTEXTS = new ArrayList<>();

	public OnBreakSpeedContext( Consumer< OnBreakSpeedData > consumer, String configName, String configComment ) {
		super( OnBreakSpeedData.class, consumer, configName, configComment );
		Context.addSorted( CONTEXTS, this );
	}

	public OnBreakSpeedContext( Consumer< OnBreakSpeedData > consumer ) {
		this( consumer, "OnBreakSpeed", "" );
	}

	@SubscribeEvent
	public static void onBreakSpeed( PlayerEvent.BreakSpeed event ) {
		Context.accept( CONTEXTS, new OnBreakSpeedData( event ) );
	}
}
