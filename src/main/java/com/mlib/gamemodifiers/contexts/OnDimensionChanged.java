package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.Context;
import com.mlib.gamemodifiers.Contexts;
import com.mlib.gamemodifiers.data.IEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class OnDimensionChanged {
	public static Context< Data > listen( Consumer< Data > consumer ) {
		return Contexts.get( Data.class ).add( consumer );
	}

	@SubscribeEvent
	public static void onDimensionChanged( PlayerEvent.PlayerChangedDimensionEvent event ) {
		Contexts.get( Data.class ).dispatch( new Data( event ) );
	}

	public static class Data implements IEntityData {
		public final PlayerEvent.PlayerChangedDimensionEvent event;
		public final LivingEntity entity;
		public final ResourceKey< Level > from;
		public final ResourceKey< Level > to;

		public Data( PlayerEvent.PlayerChangedDimensionEvent event ) {
			this.event = event;
			this.entity = event.getEntityLiving();
			this.from = event.getFrom();
			this.to = event.getTo();
		}

		@Override
		public Entity getEntity() {
			return this.entity;
		}
	}
}