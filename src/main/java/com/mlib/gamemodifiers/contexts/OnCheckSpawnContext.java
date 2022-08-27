package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.ContextBase;
import com.mlib.gamemodifiers.data.OnCheckSpawnData;
import com.mlib.gamemodifiers.parameters.ContextParameters;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class OnCheckSpawnContext extends ContextBase< OnCheckSpawnData > {
	static final List< OnCheckSpawnContext > CONTEXTS = Collections.synchronizedList( new ArrayList<>() );

	public OnCheckSpawnContext( Consumer< OnCheckSpawnData > consumer, ContextParameters params ) {
		super( OnCheckSpawnData.class, consumer, params );
		ContextBase.addSorted( CONTEXTS, this );
	}

	public OnCheckSpawnContext( Consumer< OnCheckSpawnData > consumer ) {
		this( consumer, new ContextParameters() );
	}

	@SubscribeEvent
	public static void onSpawnCheck( LivingSpawnEvent.CheckSpawn event ) {
		ContextBase.accept( CONTEXTS, new OnCheckSpawnData( event ) );
	}
}
