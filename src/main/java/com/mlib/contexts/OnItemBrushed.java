package com.mlib.contexts;

import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;
import com.mlib.contexts.data.ILevelData;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;

import java.util.function.Consumer;

public class OnItemBrushed {
	public static Context< Data > listen( Consumer< Data > consumer ) {
		return Contexts.get( Data.class ).add( consumer );
	}

	public static Data dispatch( Player player, ResourceLocation location, ItemStack itemStack, Direction direction, BrushableBlockEntity blockEntity ) {
		return Contexts.get( Data.class ).dispatch( new Data( player, location, itemStack, direction, blockEntity ) );
	}

	public static class Data implements ILevelData {
		public final Player player;
		public final ResourceLocation location;
		public final ItemStack itemStack;
		public final Direction direction;
		public final BrushableBlockEntity blockEntity;

		public Data( Player player, ResourceLocation location, ItemStack itemStack, Direction direction, BrushableBlockEntity blockEntity ) {
			this.player = player;
			this.location = location;
			this.itemStack = itemStack;
			this.direction = direction;
			this.blockEntity = blockEntity;
		}

		@Override
		public Level getLevel() {
			return this.blockEntity.getLevel();
		}
	}
}