package com.mlib.mixin;

import com.mlib.item.CustomEnchantment;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin( EnchantmentHelper.class )
public abstract class MixinEnchantmentHelper {
	@Inject(
		at = @At( "RETURN" ),
		cancellable = true,
		method = "getAvailableEnchantmentResults (ILnet/minecraft/world/item/ItemStack;Z)Ljava/util/List;"
	)
	private static void getAvailableEnchantmentResults( int $$0, ItemStack itemStack, boolean $$2,
		CallbackInfoReturnable< List< EnchantmentInstance > > callback
	) {
		List< EnchantmentInstance > enchantments = callback.getReturnValue();
		enchantments.removeIf( enchantment->enchantment.enchantment instanceof CustomEnchantment custom && !custom.canEnchantUsingEnchantingTable( itemStack ) );

		callback.setReturnValue( enchantments );
	}
}
