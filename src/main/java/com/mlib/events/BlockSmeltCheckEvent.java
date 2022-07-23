package com.mlib.events;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.IModBusEvent;

public class BlockSmeltCheckEvent extends Event implements IModBusEvent {
	public boolean shouldSmelt;
	public final ItemStack tool;
	public final BlockState blockState;
	public final Player player;

	public BlockSmeltCheckEvent( AnyLootModificationEvent event ) {
		this.shouldSmelt = false;
		this.tool = event.tool;
		this.blockState = event.blockState;
		this.player = ( Player )event.entity;
	}
}
