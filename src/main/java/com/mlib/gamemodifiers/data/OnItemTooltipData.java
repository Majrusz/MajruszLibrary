package com.mlib.gamemodifiers.data;

import com.mlib.gamemodifiers.ContextData;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

import javax.annotation.Nullable;
import java.util.List;

@Deprecated
public class OnItemTooltipData extends ContextData.Event< ItemTooltipEvent > {
	public final ItemStack itemStack;
	public final List< Component > tooltip;
	public final TooltipFlag flags;
	@Nullable public final Player player;

	public OnItemTooltipData( ItemTooltipEvent event ) {
		super( event.getPlayer(), event );
		this.itemStack = event.getItemStack();
		this.tooltip = event.getToolTip();
		this.flags = event.getFlags();
		this.player = event.getPlayer();
	}
}