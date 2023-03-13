package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.Context;
import com.mlib.gamemodifiers.Contexts;
import com.mlib.gamemodifiers.data.ILevelData;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
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

	public static class Data implements ILevelData {
		public final LivingEntityUseItemEvent.Tick event;
		public final LivingEntity entity;
		public final ItemStack itemStack;
		public final int duration;

		public Data( LivingEntityUseItemEvent.Tick event ) {
			this.event = event;
			this.entity = event.getEntity();
			this.itemStack = event.getItem();
			this.duration = event.getDuration();
		}

		@Override
		public Level getLevel() {
			return this.entity.getLevel();
		}
	}
}
