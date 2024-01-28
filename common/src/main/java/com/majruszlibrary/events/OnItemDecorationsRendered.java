package com.majruszlibrary.events;

import com.majruszlibrary.events.base.Event;
import com.majruszlibrary.events.base.Events;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

public class OnItemDecorationsRendered {
	public final ItemRenderer gui;
	public final PoseStack poseStack;
	public final Font font;
	public final ItemStack itemStack;
	public final int x;
	public final int y;

	public static Event< OnItemDecorationsRendered > listen( Consumer< OnItemDecorationsRendered > consumer ) {
		return Events.get( OnItemDecorationsRendered.class ).add( consumer );
	}

	public OnItemDecorationsRendered( ItemRenderer gui, PoseStack poseStack, Font font, ItemStack itemStack, int x, int y ) {
		this.gui = gui;
		this.poseStack = poseStack;
		this.font = font;
		this.itemStack = itemStack;
		this.x = x;
		this.y = y;
	}
}
