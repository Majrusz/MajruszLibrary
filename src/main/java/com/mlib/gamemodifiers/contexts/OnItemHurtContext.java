package com.mlib.gamemodifiers.contexts;

import com.mlib.events.ItemHurtEvent;
import com.mlib.gamemodifiers.Context;
import com.mlib.gamemodifiers.data.OnItemHurtData;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class OnItemHurtContext extends Context< OnItemHurtData > {
	static final List< OnItemHurtContext > CONTEXTS = new ArrayList<>();

	public OnItemHurtContext( Consumer< OnItemHurtData > consumer, String configName, String configComment ) {
		super( OnItemHurtData.class, consumer, configName, configComment );
		Context.addSorted( CONTEXTS, this );
	}

	public OnItemHurtContext( Consumer< OnItemHurtData > consumer ) {
		this( consumer, "OnItemHurt", "" );
	}

	@SubscribeEvent
	public static void onItemHurt( ItemHurtEvent event ) {
		Context.accept( CONTEXTS, new OnItemHurtData( event ) );
	}
}
