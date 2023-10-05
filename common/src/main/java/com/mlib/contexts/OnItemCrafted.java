package com.mlib.contexts;

import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;
import com.mlib.contexts.data.IEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

public class OnItemCrafted implements IEntityData {
	public final Player player;
	public final ItemStack crafted;
	public final CraftingContainer container;

	public static Context< OnItemCrafted > listen( Consumer< OnItemCrafted > consumer ) {
		return Contexts.get( OnItemCrafted.class ).add( consumer );
	}

	public OnItemCrafted( Player player, ItemStack crafted, CraftingContainer container ) {
		this.player = player;
		this.crafted = crafted;
		this.container = container;
	}

	@Override
	public Entity getEntity() {
		return this.player;
	}
}
