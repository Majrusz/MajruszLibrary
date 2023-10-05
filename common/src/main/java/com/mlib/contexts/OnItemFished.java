package com.mlib.contexts;

import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.function.Consumer;

public class OnItemFished {
	public final Player player;
	public final FishingHook hook;
	public final ItemStack fishingRod;
	public final List< ItemStack > items;

	public static Context< OnItemFished > listen( Consumer< OnItemFished > consumer ) {
		return Contexts.get( OnItemFished.class ).add( consumer );
	}

	public OnItemFished( Player player, FishingHook hook, ItemStack fishingRod, List< ItemStack > items ) {
		this.player = player;
		this.hook = hook;
		this.fishingRod = fishingRod;
		this.items = items;
	}
}
