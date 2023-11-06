package com.mlib.mixin;

import com.mlib.mixininterfaces.IMixinEntity;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin( LivingEntityRenderer.class )
public abstract class MixinLivingEntityRenderer {
	@Redirect(
		at = @At(
			target = "Lnet/minecraft/world/entity/LivingEntity;isSpectator ()Z",
			value = "INVOKE"
		),
		method = "render (Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V"
	)
	private boolean renderLayers( LivingEntity entity ) {
		return entity.isSpectator()
			|| ( ( Object )entity ) instanceof IMixinEntity mixin && mixin.mlib$getInvisibleTicks() > 0;
	}
}
