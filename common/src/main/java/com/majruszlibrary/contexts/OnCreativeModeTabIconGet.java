package com.majruszlibrary.contexts;

import com.majruszlibrary.contexts.base.Context;
import com.majruszlibrary.contexts.base.Contexts;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

public class OnCreativeModeTabIconGet {
	public final CreativeModeTab creativeModeTab;
	public final Component title;
	public ItemStack icon;

	public static Context< OnCreativeModeTabIconGet > listen( Consumer< OnCreativeModeTabIconGet > consumer ) {
		return Contexts.get( OnCreativeModeTabIconGet.class ).add( consumer );
	}

	public OnCreativeModeTabIconGet( CreativeModeTab creativeModeTab, Component title, ItemStack icon ) {
		this.creativeModeTab = creativeModeTab;
		this.title = title;
		this.icon = icon;
	}
}
