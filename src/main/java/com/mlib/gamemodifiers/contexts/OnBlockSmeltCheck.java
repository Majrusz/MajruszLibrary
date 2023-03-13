package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.Context;
import com.mlib.gamemodifiers.Contexts;
import com.mlib.gamemodifiers.data.ILevelData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Consumer;

public class OnBlockSmeltCheck {
	public static final Consumer< Data > ENABLE_SMELT = data->data.shouldSmelt = true;

	public static Context< Data > listen( Consumer< Data > consumer ) {
		return Contexts.get( Data.class ).add( consumer );
	}

	public static Data dispatch( ItemStack tool, BlockState blockState, Player player ) {
		return Contexts.get( Data.class ).dispatch( new Data( tool, blockState, player ) );
	}

	public static class Data implements ILevelData {
		public final ItemStack tool;
		public final BlockState blockState;
		public final Player player;
		public boolean shouldSmelt = false;

		public Data( ItemStack tool, BlockState blockState, Player player ) {
			this.tool = tool;
			this.blockState = blockState;
			this.player = player;
		}

		@Override
		public Level getLevel() {
			return this.player.getLevel();
		}
	}
}
