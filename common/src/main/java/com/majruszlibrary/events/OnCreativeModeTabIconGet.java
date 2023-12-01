package com.majruszlibrary.events;

import com.majruszlibrary.events.base.Event;
import com.majruszlibrary.events.base.Events;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

public class OnCreativeModeTabIconGet {
	public final CreativeModeTab creativeModeTab;
	public final Component title;
	public ItemStack icon;

	public static Event< OnCreativeModeTabIconGet > listen( Consumer< OnCreativeModeTabIconGet > consumer ) {
		return Events.get( OnCreativeModeTabIconGet.class ).add( consumer );
	}

	public OnCreativeModeTabIconGet( CreativeModeTab creativeModeTab, Component title, ItemStack icon ) {
		this.creativeModeTab = creativeModeTab;
		this.title = title;
		this.icon = icon;
	}
}
