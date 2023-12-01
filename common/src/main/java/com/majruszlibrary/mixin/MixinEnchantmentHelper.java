package com.majruszlibrary.mixin;

import com.majruszlibrary.events.OnLootingLevelGet;
import com.majruszlibrary.events.base.Events;
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
		callback.setReturnValue( Events.dispatch( new OnLootingLevelGet( entity, callback.getReturnValue() ) ).getLootingLevel() );
	}
}
