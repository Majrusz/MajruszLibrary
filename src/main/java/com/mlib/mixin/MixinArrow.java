package com.mlib.mixin;

import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin( Arrow.class )
public abstract class MixinArrow extends MixinProjectile {
	@Shadow( aliases = { "this$0" } )
	@Inject( method = "setEffectsFromItem(Lnet/minecraft/world/item/ItemStack;)V", at = @At( "RETURN" ) )
	private void setEffectsFromItem( ItemStack itemStack, CallbackInfo callback ) {
		this.setArrow( itemStack );
	}
}
