package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.Context;
import com.mlib.gamemodifiers.Contexts;
import com.mlib.gamemodifiers.data.ILevelData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

public class OnFarmlandTillCheck {
	public static final Consumer< Data > INCREASE_AREA = data->++data.area;

	public static Context< Data > listen( Consumer< Data > consumer ) {
		return Contexts.get( Data.class ).add( consumer );
	}

	public static Data dispatch( ServerLevel level, Player player, ItemStack itemStack ) {
		return Contexts.get( Data.class ).dispatch( new Data( level, player, itemStack ) );
	}

	public static class Data implements ILevelData {
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
		public Level getLevel() {
			return this.level;
		}
	}
}
