package com.mlib.events;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.IModBusEvent;

@Cancelable
public class FarmlandTillCheckEvent extends Event implements IModBusEvent {
	public int area = 0;
	public final ServerLevel level;
	public final Player player;
	public final ItemStack itemStack;

	public FarmlandTillCheckEvent( ServerLevel level, Player player, ItemStack itemStack ) {
		this.level = level;
		this.player = player;
		this.itemStack = itemStack;
	}
}
