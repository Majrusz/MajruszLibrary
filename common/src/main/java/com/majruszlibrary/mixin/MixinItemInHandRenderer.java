package com.majruszlibrary.mixin;

import com.majruszlibrary.events.OnItemRendered;
import com.majruszlibrary.events.base.Events;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin( ItemInHandRenderer.class )
public abstract class MixinItemInHandRenderer {
	@Inject(
		at = @At(
			ordinal = 1,
			target = "Lnet/minecraft/client/player/AbstractClientPlayer;isUsingItem ()Z",
			value = "INVOKE"
		),
		method = "renderArmWithItem (Lnet/minecraft/client/player/AbstractClientPlayer;FFLnet/minecraft/world/InteractionHand;FLnet/minecraft/world/item/ItemStack;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V"
	)
	private void renderArmWithItem( AbstractClientPlayer player, float $$1, float $$2, InteractionHand hand, float $$4, ItemStack itemStack, float $$6,
		PoseStack poseStack, MultiBufferSource $$8, int $$9, CallbackInfo callback
	) {
		Events.dispatch( new OnItemRendered( player, itemStack, hand, poseStack ) );
	}
}
