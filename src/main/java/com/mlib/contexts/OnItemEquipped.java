package com.mlib.contexts;

import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;
import com.mlib.contexts.data.IEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class OnItemEquipped {
	public static Context< Data > listen( Consumer< Data > consumer ) {
		return Contexts.get( Data.class ).add( consumer );
	}

	@SubscribeEvent
	public static void onItemEquipped( LivingEquipmentChangeEvent event ) {
		Contexts.get( Data.class ).dispatch( new Data( event ) );
	}

	public static class Data implements IEntityData {
		public final LivingEquipmentChangeEvent event;
		public final LivingEntity entity;
		public final EquipmentSlot slot;
		public final ItemStack from;
		public final ItemStack to;

		public Data( LivingEquipmentChangeEvent event ) {
			this.event = event;
			this.entity = event.getEntityLiving();
			this.slot = event.getSlot();
			this.from = event.getFrom();
			this.to = event.getTo();
		}

		@Override
		public Entity getEntity() {
			return this.entity;
		}
	}
}
