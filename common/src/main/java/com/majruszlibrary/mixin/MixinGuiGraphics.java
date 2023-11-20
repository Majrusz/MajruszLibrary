package com.majruszlibrary.mixin;

import com.majruszlibrary.contexts.OnItemDecorationsRendered;
import com.majruszlibrary.contexts.base.Contexts;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin( GuiGraphics.class )
public abstract class MixinGuiGraphics {
	@Inject(
		at = @At(
			shift = At.Shift.AFTER,
			target = "Lcom/mojang/blaze3d/vertex/PoseStack;popPose ()V",
			value = "INVOKE"
		),
		method = "renderItemDecorations (Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V"
	)
	private void renderItemDecorations( Font font, ItemStack itemStack, int xOffset, int yOffset, @Nullable String text, CallbackInfo callback ) {
		Contexts.dispatch( new OnItemDecorationsRendered( ( GuiGraphics )( Object )this, font, itemStack, xOffset, yOffset ) );
	}
}
