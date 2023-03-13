package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.Context;
import com.mlib.gamemodifiers.Contexts;
import com.mlib.gamemodifiers.data.ILevelData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class OnProjectileShot {
	public static Context< Data > listen( Consumer< Data > consumer ) {
		return Contexts.get( Data.class ).add( consumer );
	}

	public static Data dispatch( Projectile projectile, @Nullable ItemStack weapon, @Nullable ItemStack arrow, CompoundTag customTag ) {
		return Contexts.get( Data.class ).dispatch( new Data( projectile, weapon, arrow, customTag ) );
	}

	public static class Data implements ILevelData {
		public final Projectile projectile;
		public final Level level;
		@Nullable public final Entity owner;
		@Nullable public final ItemStack weapon;
		@Nullable public final ItemStack arrow;
		public final CompoundTag customTag;

		public Data( Projectile projectile, @Nullable ItemStack weapon, @Nullable ItemStack arrow, CompoundTag customTag ) {
			this.projectile = projectile;
			this.level = projectile.level;
			this.owner = projectile.getOwner();
			this.weapon = weapon;
			this.arrow = arrow;
			this.customTag = customTag;
		}

		@Override
		public Level getLevel() {
			return this.level;
		}
	}
}