package com.mlib.mixin.fabric;

import com.mlib.item.CustomEnchantment;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin( EnchantmentHelper.class )
public abstract class MixinEnchantmentHelper {
	private static ItemStack MLIB$LAST_ITEM_STACK = null;
	private static Enchantment MLIB$LAST_ENCHANTMENT = null;

	@ModifyVariable(
		at = @At( "STORE" ),
		method = "getAvailableEnchantmentResults (ILnet/minecraft/world/item/ItemStack;Z)Ljava/util/List;"
	)
	private static Enchantment getEnchantment( Enchantment enchantment ) {
		MLIB$LAST_ENCHANTMENT = enchantment;

		return enchantment;
	}

	@Inject(
		at = @At( "HEAD" ),
		method = "getAvailableEnchantmentResults (ILnet/minecraft/world/item/ItemStack;Z)Ljava/util/List;"
	)
	private static void getAvailableEnchantmentResults( int $$0, ItemStack itemStack, boolean $$2,
		CallbackInfoReturnable< List< EnchantmentInstance > > callback
	) {
		MLIB$LAST_ITEM_STACK = itemStack;
	}

	@Redirect(
		at = @At(
			target = "Lnet/minecraft/world/item/enchantment/EnchantmentCategory;canEnchant (Lnet/minecraft/world/item/Item;)Z",
			value = "INVOKE"
		),
		method = "getAvailableEnchantmentResults (ILnet/minecraft/world/item/ItemStack;Z)Ljava/util/List;"
	)
	private static boolean canEnchantUsingEnchantingTable( EnchantmentCategory category, Item item ) {
		return MLIB$LAST_ENCHANTMENT instanceof CustomEnchantment enchantment
			? enchantment.canEnchantUsingEnchantingTable( MLIB$LAST_ITEM_STACK )
			: category.canEnchant( item );
	}
}
