package com.mlib.contexts;

import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;
import com.mlib.contexts.data.IEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

public class OnFarmlandTillCheck {
	public static final Consumer< Data > INCREASE_AREA = data->++data.area;

	public static Context< Data > listen( Consumer< Data > consumer ) {
		return Contexts.get( Data.class ).add( consumer );
	}

	public static Data dispatch( ServerLevel level, Player player, ItemStack itemStack ) {
		return Contexts.get( Data.class ).dispatch( new Data( level, player, itemStack ) );
	}

	public static class Data implements IEntityData {
		public final ServerLevel level;
		public final Player player;
		public final ItemStack itemStack;
		public int area = 0;

		public Data( ServerLevel level, Player player, ItemStack itemStack ) {
			this.level = level;
			this.player = player;
			this.itemStack = itemStack;
		}

		@Override
		public Entity getEntity() {
			return this.player;
		}
	}
}
