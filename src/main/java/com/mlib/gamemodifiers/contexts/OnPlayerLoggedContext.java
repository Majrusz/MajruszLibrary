package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.ContextBase;
import com.mlib.gamemodifiers.data.OnPlayerLoggedData;
import com.mlib.gamemodifiers.parameters.ContextParameters;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class OnPlayerLoggedContext extends ContextBase< OnPlayerLoggedData > {
	static final List< OnPlayerLoggedContext > CONTEXTS = Collections.synchronizedList( new ArrayList<>() );

	public OnPlayerLoggedContext( Consumer< OnPlayerLoggedData > consumer, ContextParameters params ) {
		super( OnPlayerLoggedData.class, consumer, params );
		ContextBase.addSorted( CONTEXTS, this );
	}

	public OnPlayerLoggedContext( Consumer< OnPlayerLoggedData > consumer ) {
		this( consumer, new ContextParameters() );
	}

	@SubscribeEvent
	public static void onPlayerLogged( PlayerEvent.PlayerLoggedInEvent event ) {
		ContextBase.accept( CONTEXTS, new OnPlayerLoggedData( event ) );
	}
}
