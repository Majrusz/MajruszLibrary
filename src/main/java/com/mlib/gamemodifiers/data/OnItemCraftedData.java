package com.mlib.gamemodifiers.data;

import com.mlib.gamemodifiers.ContextData;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;

@Deprecated
public class OnItemCraftedData extends ContextData.Event< PlayerEvent.ItemCraftedEvent > {
	public final Player player;
	public final ItemStack itemStack;
	public final Container container;

	public OnItemCraftedData( PlayerEvent.ItemCraftedEvent event ) {
		super( event.getEntity(), event );
		this.player = event.getEntity();
		this.itemStack = event.getCrafting();
		this.container = event.getInventory();
	}
}
