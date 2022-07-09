package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.Context;
import com.mlib.gamemodifiers.data.OnEntityTickData;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class OnEntityTickContext extends Context< OnEntityTickData > {
	static final List< OnEntityTickContext > CONTEXTS = new ArrayList<>();

	public OnEntityTickContext( Consumer< OnEntityTickData > consumer, String configName, String configComment ) {
		super( OnEntityTickData.class, consumer, configName, configComment );
		Context.addSorted( CONTEXTS, this );
	}

	public OnEntityTickContext( Consumer< OnEntityTickData > consumer ) {
		this( consumer, "OnEntityTick", "" );
	}

	@SubscribeEvent
	public static void onEntityTick( LivingEvent.LivingUpdateEvent event ) {
		Context.accept( CONTEXTS, new OnEntityTickData( event ) );
	}
}
