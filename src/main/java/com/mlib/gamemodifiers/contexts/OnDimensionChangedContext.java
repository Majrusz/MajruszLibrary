package com.mlib.gamemodifiers.contexts;

import com.mlib.Utility;
import com.mlib.gamemodifiers.Context;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber
public class OnDimensionChangedContext extends Context {
	static final List< OnDimensionChangedContext > CONTEXTS = new ArrayList<>();

	public OnDimensionChangedContext( String configName, String configComment ) {
		super( configName, configComment );
		CONTEXTS.add( this );
	}

	public OnDimensionChangedContext() {
		this( "OnDimensionChanged", "" );
	}

	@SubscribeEvent
	public static void onDimensionChanged( PlayerEvent.PlayerChangedDimensionEvent event ) {
		handleContexts( new Data( event ), CONTEXTS );
	}

	public static class Data extends Context.Data {
		public final PlayerEvent.PlayerChangedDimensionEvent event;
		public final LivingEntity entity;
		public final ResourceKey< Level > from;
		public final ResourceKey< Level > to;

		public Data( PlayerEvent.PlayerChangedDimensionEvent event ) {
			super( event.getEntityLiving() );
			this.event = event;
			this.entity = event.getEntityLiving();
			this.from = event.getFrom();
			this.to = event.getTo();
		}
	}
}
