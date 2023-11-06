package com.mlib.mixin;

import com.mlib.contexts.OnLootingLevelGet;
import com.mlib.contexts.base.Contexts;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin( EnchantmentHelper.class )
public abstract class MixinEnchantmentHelper {
	@Inject(
		at = @At( "RETURN" ),
		cancellable = true,
		method = "getMobLooting (Lnet/minecraft/world/entity/LivingEntity;)I"
	)
	private static void getMobLooting( LivingEntity entity, CallbackInfoReturnable< Integer > callback ) {
		callback.setReturnValue( Contexts.dispatch( new OnLootingLevelGet( entity, callback.getReturnValue() ) ).getLootingLevel() );
	}
}
