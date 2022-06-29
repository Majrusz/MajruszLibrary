package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.Context;
import com.mlib.gamemodifiers.data.OnPickupXpData;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class OnPickupXpContext extends Context< OnPickupXpData > {
	static final List< OnPickupXpContext > CONTEXTS = new ArrayList<>();

	public OnPickupXpContext( Consumer< OnPickupXpData > consumer, String configName, String configComment ) {
		super( consumer, configName, configComment );
		CONTEXTS.add( this );
	}

	public OnPickupXpContext( Consumer< OnPickupXpData > consumer ) {
		this( consumer, "OnPickupXp", "" );
	}

	@SubscribeEvent public static void onPickupXp( PlayerXpEvent.PickupXp event ) {
		handleContexts( new OnPickupXpData( event ), CONTEXTS );
	}
}
