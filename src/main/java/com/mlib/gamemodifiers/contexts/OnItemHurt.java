package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.Context;
import com.mlib.gamemodifiers.Contexts;
import com.mlib.gamemodifiers.data.ILevelData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class OnItemHurt {
	public static Context< Data > listen( Consumer< Data > consumer ) {
		return Contexts.get( Data.class ).add( consumer );
	}

	public static Data dispatch( @Nullable ServerPlayer player, ItemStack itemStack, int damage ) {
		return Contexts.get( Data.class ).dispatch( new Data( player, itemStack, damage ) );
	}

	public static class Data implements ILevelData {
		@Nullable public final ServerPlayer player;
		public final ItemStack itemStack;
		public final int damage;
		public int extraDamage = 0;

		public Data( @Nullable ServerPlayer player, ItemStack itemStack, int damage ) {
			this.player = player;
			this.itemStack = itemStack;
			this.damage = damage;
		}

		public boolean isAboutToBroke() {
			return this.itemStack.getDamageValue() + this.extraDamage >= this.itemStack.getMaxDamage();
		}

		public boolean hasBeenBroken() {
			return this.itemStack.getDamageValue() >= this.itemStack.getMaxDamage();
		}

		@Override
		public Level getLevel() {
			return this.player != null ? this.player.getLevel() : null;
		}
	}
}