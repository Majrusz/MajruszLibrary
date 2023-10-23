package com.mlib.mixin;

import com.mlib.contexts.OnFishingTimeGet;
import com.mlib.contexts.base.Contexts;
import net.minecraft.world.entity.projectile.FishingHook;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin( FishingHook.class )
public abstract class MixinFishingHook {
	@Shadow private int timeUntilLured;

	@Redirect(
		at = @At(
			opcode = 181, // putfield
			ordinal = 3,
			target = "Lnet/minecraft/world/entity/projectile/FishingHook;timeUntilLured:I",
			value = "FIELD"
		),
		method = "catchingFish (Lnet/minecraft/core/BlockPos;)V"
	)
	private void catchingFish( FishingHook hook, int timeUntilLured ) {
		this.timeUntilLured = Contexts.dispatch( new OnFishingTimeGet( hook, timeUntilLured ) ).getTicks();
	}
}
