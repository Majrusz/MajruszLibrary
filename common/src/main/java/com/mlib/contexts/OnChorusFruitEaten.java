package com.mlib.contexts;

import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;
import com.mlib.contexts.data.IEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

public class OnChorusFruitEaten {
	public static Context< Data > listen( Consumer< Data > consumer ) {
		return Contexts.get( Data.class ).add( consumer );
	}

	public static Data dispatch( ItemStack itemStack, Level level, LivingEntity entity ) {
		return Contexts.get( Data.class ).dispatch( new Data( itemStack, level, entity ) );
	}

	public static class Data implements IEntityData {
		public final ItemStack itemStack;
		public final Level level;
		public final LivingEntity entity;
		private boolean isTeleportCancelled = false;

		public Data( ItemStack itemStack, Level level, LivingEntity entity ) {
			this.itemStack = itemStack;
			this.level = level;
			this.entity = entity;
		}

		@Override
		public Entity getEntity() {
			return this.entity;
		}

		public void cancelTeleport() {
			this.isTeleportCancelled = true;
		}

		public boolean isTeleportCancelled() {
			return this.isTeleportCancelled;
		}
	}
}
