package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.Context;
import com.mlib.Utility;
import com.mlib.time.Delay;
import com.mlib.time.TimeHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
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
		Data data = new Data( entity );
		Delay.onNextTick( () -> {
			for( OnSpawnedContext context : CONTEXTS ) {
				if( context.check( data ) ) {
					context.gameModifier.execute( data );
				}
			}
		} );
	}

	public static class Data extends Context.Data {
		public final LivingEntity target;
		@Nullable
		public final ServerLevel level;

		public Data( LivingEntity target ) {
			super( target );
			this.target = target;
			this.level = Utility.castIfPossible( ServerLevel.class, this.target.level );
		}
	}
}
