package com.mlib.gamemodifiers.data;

import com.mlib.gamemodifiers.ContextData;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

import java.util.List;

public class OnItemTooltipData extends ContextData.Event< ItemTooltipEvent > {
	public final ItemStack itemStack;
	public final List< Component > tooltip;
	public final TooltipFlag flags;

	public OnItemTooltipData( ItemTooltipEvent event ) {
		super( event.getEntity(), event );
		this.itemStack = event.getItemStack();
		this.tooltip = event.getToolTip();
		this.flags = event.getFlags();
	}
}