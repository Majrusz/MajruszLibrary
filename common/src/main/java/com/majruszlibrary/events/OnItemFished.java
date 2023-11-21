package com.majruszlibrary.events;

import com.majruszlibrary.events.base.Event;
import com.majruszlibrary.events.base.Events;
import com.majruszlibrary.events.type.IEntityEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.function.Consumer;

public class OnItemFished implements IEntityEvent {
	public final Player player;
	public final FishingHook hook;
	public final ItemStack fishingRod;
	public final List< ItemStack > items;

	public static Event< OnItemFished > listen( Consumer< OnItemFished > consumer ) {
		return Events.get( OnItemFished.class ).add( consumer );
	}

	public OnItemFished( Player player, FishingHook hook, ItemStack fishingRod, List< ItemStack > items ) {
		this.player = player;
		this.hook = hook;
		this.fishingRod = fishingRod;
		this.items = items;
	}

	@Override
	public Entity getEntity() {
		return this.player;
	}
}
