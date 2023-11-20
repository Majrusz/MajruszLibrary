package com.majruszlibrary.mixin;

import com.majruszlibrary.contexts.OnChorusFruitEaten;
import com.majruszlibrary.contexts.base.Contexts;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ChorusFruitItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin( ChorusFruitItem.class )
public abstract class MixinChorusFruitItem {
	@Inject(
		at = @At(
			target = "Lnet/minecraft/world/entity/LivingEntity;randomTeleport (DDDZ)Z",
			value = "INVOKE"
		),
		cancellable = true,
		locals = LocalCapture.CAPTURE_FAILHARD,
		method = "finishUsingItem (Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;)Lnet/minecraft/world/item/ItemStack;"
	)
	private void finishUsingItem( ItemStack itemStack, Level level, LivingEntity entity, CallbackInfoReturnable< ItemStack > callback, ItemStack result ) {
		if( Contexts.dispatch( new OnChorusFruitEaten( result, level, entity ) ).isTeleportCancelled() ) {
			callback.setReturnValue( result );
		}
	}
}
