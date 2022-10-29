package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.ContextBase;
import com.mlib.gamemodifiers.ContextData;
import com.mlib.gamemodifiers.parameters.ContextParameters;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class OnDimensionChanged {
	@Mod.EventBusSubscriber
	public static class Context extends ContextBase< Data > {
		static final List< Context > CONTEXTS = Collections.synchronizedList( new ArrayList<>() );

		public Context( Consumer< Data > consumer, ContextParameters params ) {
			super( Data.class, consumer, params );
			ContextBase.addSorted( CONTEXTS, this );
		}

		public Context( Consumer< Data > consumer ) {
			this( consumer, new ContextParameters() );
		}

		@SubscribeEvent
		public static void onDimensionChanged( PlayerEvent.PlayerChangedDimensionEvent event ) {
			ContextBase.accept( CONTEXTS, new Data( event ) );
		}
	}

	public static class Data extends ContextData.Event< PlayerEvent.PlayerChangedDimensionEvent > {
		public final LivingEntity entity;
		public final ResourceKey< Level > from;
		public final ResourceKey< Level > to;

		public Data( PlayerEvent.PlayerChangedDimensionEvent event ) {
			super( event.getEntity(), event );
			this.entity = event.getEntity();
			this.from = event.getFrom();
			this.to = event.getTo();
		}
	}
}