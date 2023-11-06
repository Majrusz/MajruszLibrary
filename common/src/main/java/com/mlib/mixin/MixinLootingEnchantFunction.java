package com.mlib.mixin;

import com.mlib.contexts.OnLootingLevelGet;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin( LootingEnchantFunction.class )
public abstract class MixinLootingEnchantFunction {
	@Inject(
		at = @At( "HEAD" ),
		method = "run (Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/storage/loot/LootContext;)Lnet/minecraft/world/item/ItemStack;"
	)
	private void run( ItemStack itemStack, LootContext context, CallbackInfoReturnable< ItemStack > callback ) {
		OnLootingLevelGet.Cache.SOURCE = context.getParamOrNull( LootContextParams.DAMAGE_SOURCE );
	}
}
