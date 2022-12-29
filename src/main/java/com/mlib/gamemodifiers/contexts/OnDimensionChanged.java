package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.ContextBase;
import com.mlib.gamemodifiers.ContextData;
import com.mlib.gamemodifiers.Contexts;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Consumer;

public class OnDimensionChanged {
	@Mod.EventBusSubscriber
	public static class Context extends ContextBase< Data > {
		static final Contexts< Data, Context > CONTEXTS = new Contexts<>();

		public Context( Consumer< Data > consumer ) {
			super( consumer );

			CONTEXTS.add( this );
		}

		@SubscribeEvent
		public static void onDimensionChanged( PlayerEvent.PlayerChangedDimensionEvent event ) {
			CONTEXTS.accept( new Data( event ) );
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