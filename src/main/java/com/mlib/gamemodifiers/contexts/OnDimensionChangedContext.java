package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.Context;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

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
		handleContexts( context->new Data( context, event ), CONTEXTS );
	}

	public static class Data extends Context.Data {
		public final PlayerEvent.PlayerChangedDimensionEvent event;
		public final LivingEntity entity;
		public final ResourceKey< Level > from;
		public final ResourceKey< Level > to;

		Data( Context context, PlayerEvent.PlayerChangedDimensionEvent event ) {
			super( context, event.getEntityLiving() );
			this.event = event;
			this.entity = event.getEntityLiving();
			this.from = event.getFrom();
			this.to = event.getTo();
		}
	}
}
