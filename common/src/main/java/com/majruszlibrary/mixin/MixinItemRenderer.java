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
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin( ItemRenderer.class )
public abstract class MixinItemRenderer {
	@Inject(
		at = @At( "TAIL" ),
		method = "renderGuiItemDecorations (Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V"
	)
	private void renderGuiItemDecorations( Font font, ItemStack itemStack, int xOffset, int yOffset, @Nullable String text, CallbackInfo callback ) {
		if( itemStack.isEmpty() ) {
			return;
		}

		Events.dispatch( new OnItemDecorationsRendered( ( ItemRenderer )( Object )this, font, itemStack, xOffset, yOffset ) );
	}
}
