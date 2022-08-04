package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.Context;
import com.mlib.gamemodifiers.data.OnSpawnedData;
import com.mlib.gamemodifiers.parameters.ContextParameters;
import com.mlib.time.Delay;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class OnSpawnedContext extends Context< OnSpawnedData > {
	static final List< OnSpawnedContext > CONTEXTS = new ArrayList<>();

	public OnSpawnedContext( Consumer< OnSpawnedData > consumer, ContextParameters params ) {
		super( OnSpawnedData.class, consumer, params );
		Context.addSorted( CONTEXTS, this );
	}

	public OnSpawnedContext( Consumer< OnSpawnedData > consumer ) {
		this( consumer, new ContextParameters() );
	}

	@SubscribeEvent
	public static void onSpawn( EntityJoinWorldEvent event ) {
		if( !( event.getEntity() instanceof LivingEntity entity ) || !( event.getWorld() instanceof ServerLevel ) )
			return;

		// it does not contain an event, and it is delayed on purpose because otherwise it could cause deadlocks on chunks with any incorrect access (see EntityJoinWorldEvent for more info)
		Delay.onNextTick( ()->Context.accept( CONTEXTS, new OnSpawnedData( entity, event.loadedFromDisk() ) ) );
	}
}
