package com.majruszlibrary.mixin;

import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin( Arrow.class )
public abstract class MixinArrow extends MixinProjectile {
	@Inject(
		at = @At( "RETURN" ),
		method = "setEffectsFromItem (Lnet/minecraft/world/item/ItemStack;)V"
	)
	private void setEffectsFromItem( ItemStack itemStack, CallbackInfo callback ) {
		this.majruszlibrary$update( itemStack );
	}
}
