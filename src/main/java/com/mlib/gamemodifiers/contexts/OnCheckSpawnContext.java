package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.Context;
import com.mlib.gamemodifiers.data.OnCheckSpawnData;
import com.mlib.gamemodifiers.parameters.ContextParameters;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class OnCheckSpawnContext extends Context< OnCheckSpawnData > {
	static final List< OnCheckSpawnContext > CONTEXTS = new ArrayList<>();

	public OnCheckSpawnContext( Consumer< OnCheckSpawnData > consumer, ContextParameters params ) {
		super( OnCheckSpawnData.class, consumer, params );
		Context.addSorted( CONTEXTS, this );
	}

	public OnCheckSpawnContext( Consumer< OnCheckSpawnData > consumer ) {
		this( consumer, new ContextParameters() );
	}

	@SubscribeEvent
	public static void onSpawnCheck( LivingSpawnEvent.CheckSpawn event ) {
		Context.accept( CONTEXTS, new OnCheckSpawnData( event ) );
	}
}
