package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.ContextBase;
import com.mlib.gamemodifiers.ContextData;
import com.mlib.gamemodifiers.Contexts;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class OnFoodPropertiesGet {
	static final Contexts< Data, Context > CONTEXTS = new Contexts<>();

	public static void broadcast( Data data ) {
		CONTEXTS.accept( data );
	}

	public static class Context extends ContextBase< Data > {
		public Context( Consumer< Data > consumer ) {
			super( consumer );

			CONTEXTS.add( this );
		}
	}

	public static class Data extends ContextData {
		public FoodProperties properties;
		public ItemStack itemStack;
		@Nullable public LivingEntity entity;

		public Data( FoodProperties properties, ItemStack itemStack, @Nullable LivingEntity entity ) {
			super( entity );

			this.properties = properties;
			this.itemStack = itemStack;
			this.entity = entity;
		}
	}
}
