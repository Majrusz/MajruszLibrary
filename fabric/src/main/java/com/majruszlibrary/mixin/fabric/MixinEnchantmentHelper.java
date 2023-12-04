package com.majruszlibrary.mixin.fabric;

import com.majruszlibrary.item.CustomEnchantment;
import com.majruszlibrary.registry.Registries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin( EnchantmentHelper.class )
public abstract class MixinEnchantmentHelper {
	// TODO: in Fabric 0.15.0+ there will be a native support for MixinExtras that can be used to make it cleaner
	@Inject(
		at = @At( "RETURN" ),
		cancellable = true,
		method = "getAvailableEnchantmentResults (ILnet/minecraft/world/item/ItemStack;Z)Ljava/util/List;"
	)
	private static void getAvailableEnchantmentResults( int level, ItemStack itemStack, boolean isTreasure,
		CallbackInfoReturnable< List< EnchantmentInstance > > callback
	) {
		List< EnchantmentInstance > enchantments = callback.getReturnValue();
		enchantments.removeIf( enchantment->enchantment.enchantment instanceof CustomEnchantment );
		boolean isBook = itemStack.is( Items.BOOK );

		for( Enchantment enchantment : Registries.ENCHANTMENTS ) {
			if( !( enchantment instanceof CustomEnchantment customEnchantment ) ) {
				continue;
			}

			if( enchantment.isTreasureOnly() && !isTreasure ) {
				continue;
			}

			if( !enchantment.isDiscoverable() ) {
				continue;
			}

			if( !customEnchantment.canEnchantUsingEnchantingTable( itemStack ) && !isBook ) {
				continue;
			}

			for( int enchantmentLevel = enchantment.getMaxLevel(); enchantmentLevel > enchantment.getMinLevel() - 1; --enchantmentLevel ) {
				if( level >= enchantment.getMinCost( enchantmentLevel ) && level <= enchantment.getMaxCost( enchantmentLevel ) ) {
					enchantments.add( new EnchantmentInstance( enchantment, enchantmentLevel ) );
					break;
				}
			}
		}

		callback.setReturnValue( enchantments );
	}
}
