package com.majruszlibrary.mixin;

import com.majruszlibrary.events.OnItemEntityRendered;
import com.majruszlibrary.events.base.Events;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin( BlockEntityWithoutLevelRenderer.class )
public abstract class MixinBlockEntityWithoutLevelRenderer {
	@Inject(
		at = @At( "RETURN" ),
		method = "renderByItem (Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/client/renderer/block/model/ItemTransforms$TransformType;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V"
	)
	private void renderByItem( ItemStack itemStack, ItemTransforms.TransformType transformType, PoseStack poseStack, MultiBufferSource multiBufferSource, int x, int y,
		CallbackInfo callback
	) {
		Events.dispatch( new OnItemEntityRendered( itemStack, transformType, poseStack, multiBufferSource, x, y ) );
	}
}
