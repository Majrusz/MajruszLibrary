package com.majruszlibrary.mixin.forge;

import com.majruszlibrary.item.CustomEnchantment;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin( Enchantment.class )
public abstract class MixinEnchantment {
	@Inject(
		at = @At( "RETURN" ),
		cancellable = true,
		method = "canApplyAtEnchantingTable (Lnet/minecraft/world/item/ItemStack;)Z",
		remap = false
	)
	private void canApplyAtEnchantingTable( ItemStack itemStack, CallbackInfoReturnable< Boolean > callback ) {
		if( ( ( Object )this ) instanceof CustomEnchantment enchantment ) {
			callback.setReturnValue( enchantment.canEnchantUsingEnchantingTable( itemStack ) );
		}
	}
}
