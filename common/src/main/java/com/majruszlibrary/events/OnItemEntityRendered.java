package com.majruszlibrary.events;

import com.majruszlibrary.annotation.Dist;
import com.majruszlibrary.annotation.OnlyIn;
import com.majruszlibrary.events.base.Event;
import com.majruszlibrary.events.base.Events;
import com.majruszlibrary.platform.Side;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

@OnlyIn( Dist.CLIENT )
public class OnItemEntityRendered {
	public final LocalPlayer player;
	public final ItemStack itemStack;
	public final ItemDisplayContext context;
	public final PoseStack poseStack;
	public final MultiBufferSource multiBufferSource;
	public final int x;
	public final int y;

	public static Event< OnItemEntityRendered > listen( Consumer< OnItemEntityRendered > consumer ) {
		return Events.get( OnItemEntityRendered.class ).add( consumer );
	}

	public OnItemEntityRendered( ItemStack itemStack, ItemDisplayContext context, PoseStack poseStack, MultiBufferSource multiBufferSource, int x, int y ) {
		this.player = Side.getLocalPlayer();
		this.itemStack = itemStack;
		this.context = context;
		this.poseStack = poseStack;
		this.multiBufferSource = multiBufferSource;
		this.x = x;
		this.y = y;
	}
}
