package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.ContextBase;
import com.mlib.gamemodifiers.data.OnSpawnedData;
import com.mlib.gamemodifiers.parameters.ContextParameters;
import com.mlib.time.Delay;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class OnSpawnedContext extends ContextBase< OnSpawnedData > {
	static final List< OnSpawnedContext > CONTEXTS = new ArrayList<>();

	public OnSpawnedContext( Consumer< OnSpawnedData > consumer, ContextParameters params ) {
		super( OnSpawnedData.class, consumer, params );
		ContextBase.addSorted( CONTEXTS, this );
	}

	public OnSpawnedContext( Consumer< OnSpawnedData > consumer ) {
		this( consumer, new ContextParameters() );
	}

	@SubscribeEvent
	public static void onSpawn( EntityJoinLevelEvent event ) {
		if( !( event.getEntity() instanceof LivingEntity entity ) || !( event.getLevel() instanceof ServerLevel ) )
			return;

		// it does not contain an event, and it is delayed on purpose because otherwise it could cause deadlocks on chunks with any incorrect access (see EntityJoinWorldEvent for more info)
		Delay.onNextTick( ()->ContextBase.accept( CONTEXTS, new OnSpawnedData( entity, event.loadedFromDisk() ) ) );
	}
}
