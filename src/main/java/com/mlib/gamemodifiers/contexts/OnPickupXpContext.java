package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.Context;
import com.mlib.gamemodifiers.data.OnPickupXpData;
import com.mlib.gamemodifiers.parameters.ContextParameters;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class OnPickupXpContext extends Context< OnPickupXpData > {
	static final List< OnPickupXpContext > CONTEXTS = new ArrayList<>();

	public OnPickupXpContext( Consumer< OnPickupXpData > consumer, ContextParameters params ) {
		super( OnPickupXpData.class, consumer, params );
		Context.addSorted( CONTEXTS, this );
	}

	public OnPickupXpContext( Consumer< OnPickupXpData > consumer ) {
		this( consumer, new ContextParameters() );
	}

	@SubscribeEvent
	public static void onPickupXp( PlayerXpEvent.PickupXp event ) {
		Context.accept( CONTEXTS, new OnPickupXpData( event ) );
	}
}
