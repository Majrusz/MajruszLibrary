package com.mlib.contexts;

import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class OnItemTooltip {
	public static Context< Data > listen( Consumer< Data > consumer ) {
		return Contexts.get( Data.class ).add( consumer );
	}

	@SubscribeEvent
	public static void onItemTooltip( ItemTooltipEvent event ) {
		Contexts.get( Data.class ).dispatch( new Data( event ) );
	}

	public static class Data {
		public final ItemTooltipEvent event;
		public final ItemStack itemStack;
		public final List< Component > tooltip;
		public final TooltipFlag flags;
		@Nullable public final Player player;

		public Data( ItemTooltipEvent event ) {
			this.event = event;
			this.itemStack = event.getItemStack();
			this.tooltip = event.getToolTip();
			this.flags = event.getFlags();
			this.player = event.getPlayer();
		}
	}
}
