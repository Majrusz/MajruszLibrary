package com.majruszlibrary.events;

import com.majruszlibrary.annotation.Dist;
import com.majruszlibrary.annotation.OnlyIn;
import com.majruszlibrary.events.base.Event;
import com.majruszlibrary.events.base.Events;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

@OnlyIn( Dist.CLIENT )
public class OnItemEntityRendered {
	public final ItemStack itemStack;
	public final ItemTransforms.TransformType transformType;
	public final PoseStack poseStack;
	public final MultiBufferSource multiBufferSource;
	public final int x;
	public final int y;

	public static Event< OnItemEntityRendered > listen( Consumer< OnItemEntityRendered > consumer ) {
		return Events.get( OnItemEntityRendered.class ).add( consumer );
	}

	public OnItemEntityRendered( ItemStack itemStack, ItemTransforms.TransformType transformType, PoseStack poseStack, MultiBufferSource multiBufferSource, int x, int y ) {
		this.itemStack = itemStack;
		this.transformType = transformType;
		this.poseStack = poseStack;
		this.multiBufferSource = multiBufferSource;
		this.x = x;
		this.y = y;
	}
}
