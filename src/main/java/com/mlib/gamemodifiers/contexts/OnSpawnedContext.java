package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.Context;
import com.mlib.Utility;
import com.mlib.time.TimeHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber
public class OnSpawnedContext extends Context {
	static final List< OnSpawnedContext > CONTEXTS = new ArrayList<>();
	static final String MARK_TAG = "OnSpawnedContextEntityMarked";

	public OnSpawnedContext( String configName, String configComment ) {
		super( configName, configComment );
		CONTEXTS.add( this );
	}

	public OnSpawnedContext() {
		this( "OnSpawned", "" );
	}

	@SubscribeEvent
	public static void onSpawn( LivingSpawnEvent.SpecialSpawn event ) {
		handleModifiers( event, event.getEntityLiving() );
	}

	@SubscribeEvent
	public static void onTick( TickEvent.WorldTickEvent event ) {
		if( !( event.world instanceof ServerLevel level ) || !TimeHelper.hasServerSecondsPassed(  10.0 ) )
			return;

		for( Entity entity : level.getEntities().getAll() )
			if( entity instanceof LivingEntity livingEntity && !isMarked( entity ) )
				handleModifiers( null, livingEntity );
	}

	private static void handleModifiers( @Nullable LivingSpawnEvent.SpecialSpawn event, LivingEntity target ) {
		markEntity( target );
		Data data = new Data( event, target );

		for( OnSpawnedContext context : CONTEXTS ) {
			if( context.check( data ) ) {
				context.gameModifier.execute( data );
			}
		}
	}

	private static void markEntity( Entity entity ) {
		entity.getPersistentData().putBoolean( MARK_TAG, true );
	}

	private static boolean isMarked( Entity entity ) {
		return entity.getPersistentData().getBoolean( MARK_TAG );
	}

	public static class Data extends Context.Data {
		@Nullable
		public final LivingSpawnEvent.SpecialSpawn event;
		public final LivingEntity target;
		@Nullable
		public final ServerLevel level;

		public Data( @Nullable LivingSpawnEvent.SpecialSpawn event, LivingEntity target ) {
			super( target );
			this.event = event;
			this.target = target;
			this.level = Utility.castIfPossible( ServerLevel.class, this.target.level );
		}
	}
}
