package com.mlib.contexts;

import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;
import com.mlib.contexts.data.IEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class OnUseItemTick {
	public static Context< Data > listen( Consumer< Data > consumer ) {
		return Contexts.get( Data.class ).add( consumer );
	}

	@SubscribeEvent
	public static void onTick( LivingEntityUseItemEvent.Tick event ) {
		Contexts.get( Data.class ).dispatch( new Data( event ) );
	}

	public static class Data implements IEntityData {
		public final LivingEntityUseItemEvent.Tick event;
		public final LivingEntity entity;
		public final ItemStack itemStack;
		public final int duration;

		public Data( LivingEntityUseItemEvent.Tick event ) {
			this.event = event;
			this.entity = event.getEntityLiving();
			this.itemStack = event.getItem();
			this.duration = event.getDuration();
		}

		@Override
		public Entity getEntity() {
			return this.entity;
		}
	}
}
