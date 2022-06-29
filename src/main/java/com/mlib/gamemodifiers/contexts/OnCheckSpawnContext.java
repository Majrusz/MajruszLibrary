package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.Context;
import com.mlib.gamemodifiers.data.OnCheckSpawnData;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class OnCheckSpawnContext extends Context< OnCheckSpawnData > {
	static final List< OnCheckSpawnContext > CONTEXTS = new ArrayList<>();

	public OnCheckSpawnContext( Consumer< OnCheckSpawnData > consumer, String configName, String configComment ) {
		super( consumer, configName, configComment );
		CONTEXTS.add( this );
	}

	public OnCheckSpawnContext( Consumer< OnCheckSpawnData > consumer ) {
		this( consumer, "OnCheckSpawn", "" );
	}

	@SubscribeEvent public static void onSpawnCheck( LivingSpawnEvent.CheckSpawn event ) {
		handleContexts( new OnCheckSpawnData( event ), CONTEXTS );
	}
}
