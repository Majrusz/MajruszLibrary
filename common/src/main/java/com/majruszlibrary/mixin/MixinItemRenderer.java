package com.majruszlibrary.mixin;

import com.majruszlibrary.events.OnItemDecorationsRendered;
import com.majruszlibrary.events.base.Events;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin( ItemRenderer.class )
public abstract class MixinItemRenderer {
	@Inject(
		at = @At(
			shift = At.Shift.AFTER,
			target = "Lcom/mojang/blaze3d/vertex/PoseStack;popPose ()V",
			value = "INVOKE"
		),
		method = "renderGuiItemDecorations (Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V"
	)
	private void renderGuiItemDecorations( PoseStack poseStack, Font font, ItemStack itemStack, int xOffset, int yOffset, @Nullable String text, CallbackInfo callback ) {
		Events.dispatch( new OnItemDecorationsRendered( ( ItemRenderer )( Object )this, poseStack, font, itemStack, xOffset, yOffset ) );
	}
}
