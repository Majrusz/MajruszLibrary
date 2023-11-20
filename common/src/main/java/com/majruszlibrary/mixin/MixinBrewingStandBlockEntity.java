package com.majruszlibrary.mixin;

import com.majruszlibrary.contexts.OnItemBrewed;
import com.majruszlibrary.contexts.base.Contexts;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BrewingStandBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin( BrewingStandBlockEntity.class )
public abstract class MixinBrewingStandBlockEntity {
	@Inject(
		at = @At(
			target = "Lnet/minecraft/world/item/ItemStack;shrink (I)V",
			value = "INVOKE"
		),
		method = "doBrew (Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/NonNullList;)V"
	)
	private static void doBrew( Level level, BlockPos blockPos, NonNullList< ItemStack > items, CallbackInfo callback ) {
		Contexts.dispatch( new OnItemBrewed( level, blockPos, items ) );
	}
}
