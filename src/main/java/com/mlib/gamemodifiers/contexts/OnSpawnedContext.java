package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.Context;
import com.mlib.time.Delay;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber
public class OnSpawnedContext extends Context {
	static final List< OnSpawnedContext > CONTEXTS = new ArrayList<>();

	public OnSpawnedContext( String configName, String configComment ) {
		super( configName, configComment );
		CONTEXTS.add( this );
	}

	public OnSpawnedContext() {
		this( "OnSpawned", "" );
	}

	@SubscribeEvent
	public static void onSpawn( EntityJoinWorldEvent event ) {
		if( !( event.getEntity() instanceof LivingEntity entity ) || !( event.getWorld() instanceof ServerLevel ) )
			return;

		// it does not contain an event, and it is delayed on purpose because otherwise it could cause deadlocks on chunks with any incorrect access see EntityJoinWorldEvent for more info
		Delay.onNextTick( ()->handleContexts( context->new Data( context, entity, event.loadedFromDisk() ), CONTEXTS ) );
	}

	public static class Data extends Context.Data {
		public final LivingEntity target;
		public final boolean loadedFromDisk;

		Data( Context context, LivingEntity target, boolean loadedFromDisk ) {
			super( context, target );
			this.target = target;
			this.loadedFromDisk = loadedFromDisk;
		}
	}
}
