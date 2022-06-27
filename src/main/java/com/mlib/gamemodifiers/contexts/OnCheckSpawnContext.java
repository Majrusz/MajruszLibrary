package com.mlib.gamemodifiers.contexts;

import com.mlib.Utility;
import com.mlib.gamemodifiers.Context;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber
public class OnCheckSpawnContext extends Context {
	static final List< OnCheckSpawnContext > CONTEXTS = new ArrayList<>();

	public OnCheckSpawnContext( String configName, String configComment ) {
		super( configName, configComment );
		CONTEXTS.add( this );
	}

	public OnCheckSpawnContext() {
		this( "OnCheckSpawn", "" );
	}

	@SubscribeEvent
	public static void onSpawnCheck( LivingSpawnEvent.CheckSpawn event ) {
		handleContexts( context->new Data( context, event ), CONTEXTS );
	}

	public static class Data extends Context.Data {
		public final LivingSpawnEvent.CheckSpawn event;
		public final LivingEntity entity;
		@Nullable
		public final ServerLevel level;

		public Data( Context context, LivingSpawnEvent.CheckSpawn event ) {
			super( context, event.getEntityLiving() );
			this.event = event;
			this.entity = event.getEntityLiving();
			this.level = Utility.castIfPossible( ServerLevel.class, this.entity.level );
		}
	}
}
