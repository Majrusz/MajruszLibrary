package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.ContextBase;
import com.mlib.gamemodifiers.data.OnPickupXpData;
import com.mlib.gamemodifiers.parameters.ContextParameters;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class OnPickupXpContext extends ContextBase< OnPickupXpData > {
	static final List< OnPickupXpContext > CONTEXTS = Collections.synchronizedList( new ArrayList<>() );

	public OnPickupXpContext( Consumer< OnPickupXpData > consumer, ContextParameters params ) {
		super( OnPickupXpData.class, consumer, params );
		ContextBase.addSorted( CONTEXTS, this );
	}

	public OnPickupXpContext( Consumer< OnPickupXpData > consumer ) {
		this( consumer, new ContextParameters() );
	}

	@SubscribeEvent
	public static void onPickupXp( PlayerXpEvent.PickupXp event ) {
		ContextBase.accept( CONTEXTS, new OnPickupXpData( event ) );
	}
}
