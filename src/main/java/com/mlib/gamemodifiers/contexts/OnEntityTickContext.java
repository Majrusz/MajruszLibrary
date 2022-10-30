package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.ContextBase;
import com.mlib.gamemodifiers.data.OnEntityTickData;
import com.mlib.gamemodifiers.parameters.ContextParameters;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

@Deprecated
@Mod.EventBusSubscriber
public class OnEntityTickContext extends ContextBase< OnEntityTickData > {
	static final List< OnEntityTickContext > CONTEXTS = Collections.synchronizedList( new ArrayList<>() );

	public OnEntityTickContext( Consumer< OnEntityTickData > consumer, ContextParameters params ) {
		super( OnEntityTickData.class, consumer, params );
		ContextBase.addSorted( CONTEXTS, this );
	}

	public OnEntityTickContext( Consumer< OnEntityTickData > consumer ) {
		this( consumer, new ContextParameters() );
	}

	@SubscribeEvent
	public static void onEntityTick( LivingEvent.LivingTickEvent event ) {
		ContextBase.accept( CONTEXTS, new OnEntityTickData( event ) );
	}
}
