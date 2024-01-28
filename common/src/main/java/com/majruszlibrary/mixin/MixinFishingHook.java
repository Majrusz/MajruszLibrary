package com.majruszlibrary.mixin;

import com.majruszlibrary.events.OnFishingTimeGet;
import com.majruszlibrary.events.base.Events;
import net.minecraft.world.entity.projectile.FishingHook;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin( FishingHook.class )
public abstract class MixinFishingHook {
	private @Shadow int timeUntilLured;

	@Redirect(
		at = @At(
			opcode = 181, // putfield
			ordinal = 2,
			target = "Lnet/minecraft/world/entity/projectile/FishingHook;timeUntilLured:I",
			value = "FIELD"
		),
		method = "catchingFish (Lnet/minecraft/core/BlockPos;)V"
	)
	private void catchingFish( FishingHook hook, int timeUntilLured ) {
		this.timeUntilLured = Events.dispatch( new OnFishingTimeGet( hook, timeUntilLured ) ).getTicks();
	}
}
