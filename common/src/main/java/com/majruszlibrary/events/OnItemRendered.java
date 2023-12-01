package com.majruszlibrary.events;

import com.majruszlibrary.annotation.Dist;
import com.majruszlibrary.annotation.OnlyIn;
import com.majruszlibrary.events.base.Event;
import com.majruszlibrary.events.base.Events;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

@OnlyIn( Dist.CLIENT )
public class OnItemRendered {
	public final AbstractClientPlayer player;
	public final ItemStack itemStack;
	public final InteractionHand hand;
	public final PoseStack poseStack;

	public static Event< OnItemRendered > listen( Consumer< OnItemRendered > consumer ) {
		return Events.get( OnItemRendered.class ).add( consumer );
	}

	public OnItemRendered( AbstractClientPlayer player, ItemStack itemStack, InteractionHand hand, PoseStack poseStack ) {
		this.player = player;
		this.itemStack = itemStack;
		this.hand = hand;
		this.poseStack = poseStack;
	}
}
