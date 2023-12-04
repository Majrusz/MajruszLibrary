package com.majruszlibrary.events;

import com.majruszlibrary.events.base.Event;
import com.majruszlibrary.events.base.Events;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

@Deprecated( since = "7.0.1 use OnItemRenderColorsGet" )
public class OnItemRenderColorGet {
	public final ItemStack itemStack;
	public final int layerIdx;
	public final int original;
	public int color;

	public static Event< OnItemRenderColorGet > listen( Consumer< OnItemRenderColorGet > consumer ) {
		return Events.get( OnItemRenderColorGet.class ).add( consumer );
	}

	public OnItemRenderColorGet( ItemStack itemStack, int layerIdx, int color ) {
		this.itemStack = itemStack;
		this.layerIdx = layerIdx;
		this.original = color;
		this.color = color;
	}

	public int getColor() {
		return this.color;
	}
}
