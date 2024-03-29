package com.majruszlibrary.events;

import com.majruszlibrary.events.base.Event;
import com.majruszlibrary.events.base.Events;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class OnItemTooltip {
	public final ItemStack itemStack;
	public final List< Component > components;
	public final TooltipFlag flags;
	public final @Nullable Player player;

	public static Event< OnItemTooltip > listen( Consumer< OnItemTooltip > consumer ) {
		return Events.get( OnItemTooltip.class ).add( consumer );
	}

	public OnItemTooltip( ItemStack itemStack, List< Component > components, TooltipFlag flags, @Nullable Player player ) {
		this.itemStack = itemStack;
		this.components = components;
		this.flags = flags;
		this.player = player;
	}

	public boolean isAdvanced() {
		return this.flags.isAdvanced();
	}
}
