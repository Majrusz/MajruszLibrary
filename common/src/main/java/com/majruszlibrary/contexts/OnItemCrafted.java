package com.majruszlibrary.contexts;

import com.majruszlibrary.contexts.base.Context;
import com.majruszlibrary.contexts.base.Contexts;
import com.majruszlibrary.contexts.data.IEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

public class OnItemCrafted implements IEntityData {
	public final Player player;
	public final ItemStack itemStack;

	public static Context< OnItemCrafted > listen( Consumer< OnItemCrafted > consumer ) {
		return Contexts.get( OnItemCrafted.class ).add( consumer );
	}

	public OnItemCrafted( Player player, ItemStack itemStack ) {
		this.player = player;
		this.itemStack = itemStack;
	}

	@Override
	public Entity getEntity() {
		return this.player;
	}
}
