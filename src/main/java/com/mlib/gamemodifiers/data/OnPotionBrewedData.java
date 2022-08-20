package com.mlib.gamemodifiers.data;

import com.mlib.gamemodifiers.ContextData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.brewing.PlayerBrewedPotionEvent;

public class OnPotionBrewedData extends ContextData.Event< PlayerBrewedPotionEvent > {
	public final Player player;
	public final ItemStack itemStack;

	public OnPotionBrewedData( PlayerBrewedPotionEvent event ) {
		super( event.getEntity(), event );
		this.player = event.getEntity();
		this.itemStack = event.getStack();
	}
}
