package com.mlib.mixin;

import com.mlib.contexts.OnLootingLevelGet;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithLootingCondition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin( LootItemRandomChanceWithLootingCondition.class )
public abstract class MixinLootItemRandomChanceWithLootingCondition {
	@Inject(
		at = @At( "HEAD" ),
		method = "test (Lnet/minecraft/world/level/storage/loot/LootContext;)Z"
	)
	private void test( LootContext context, CallbackInfoReturnable< Boolean > callback ) {
		OnLootingLevelGet.Cache.SOURCE = context.getParamOrNull( LootContextParams.DAMAGE_SOURCE );
	}
}
