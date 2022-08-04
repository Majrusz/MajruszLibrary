package com.mlib.gamemodifiers.data;

import com.mlib.gamemodifiers.ContextData;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class OnItemCraftedData extends ContextData.Event< PlayerEvent.ItemCraftedEvent > {
	public final Player player;
	public final ItemStack itemStack;
	public final Container container;

	public OnItemCraftedData( PlayerEvent.ItemCraftedEvent event ) {
		super( event.getEntityLiving(), event );
		this.player = event.getPlayer();
		this.itemStack = event.getCrafting();
		this.container = event.getInventory();
	}
}
