package com.mlib.contexts;

import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class OnFoodPropertiesGet {
	public static Context< Data > listen( Consumer< Data > consumer ) {
		return Contexts.get( Data.class ).add( consumer );
	}

	public static Data dispatch( FoodProperties properties, ItemStack itemStack, @Nullable LivingEntity entity ) {
		return Contexts.get( Data.class ).dispatch( new Data( properties, itemStack, entity ) );
	}

	public static class Data {
		public FoodProperties properties;
		public ItemStack itemStack;
		@Nullable public LivingEntity entity;

		public Data( FoodProperties properties, ItemStack itemStack, @Nullable LivingEntity entity ) {
			this.properties = properties;
			this.itemStack = itemStack;
			this.entity = entity;
		}
	}
}
