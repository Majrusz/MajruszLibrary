package com.majruszlibrary.events;

import com.majruszlibrary.events.base.Event;
import com.majruszlibrary.events.base.Events;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

public class OnItemDecorationsRendered {
	public final GuiGraphics gui;
	public final Font font;
	public final ItemStack itemStack;
	public final int x;
	public final int y;

	public static Event< OnItemDecorationsRendered > listen( Consumer< OnItemDecorationsRendered > consumer ) {
		return Events.get( OnItemDecorationsRendered.class ).add( consumer );
	}

	public OnItemDecorationsRendered( GuiGraphics gui, Font font, ItemStack itemStack, int x, int y ) {
		this.gui = gui;
		this.font = font;
		this.itemStack = itemStack;
		this.x = x;
		this.y = y;
	}
}
