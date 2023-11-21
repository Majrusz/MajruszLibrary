package com.majruszlibrary.events;

import com.majruszlibrary.events.base.Event;
import com.majruszlibrary.events.base.Events;
import com.majruszlibrary.events.type.IEntityEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

public class OnItemCrafted implements IEntityEvent {
	public final Player player;
	public final ItemStack itemStack;

	public static Event< OnItemCrafted > listen( Consumer< OnItemCrafted > consumer ) {
		return Events.get( OnItemCrafted.class ).add( consumer );
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
