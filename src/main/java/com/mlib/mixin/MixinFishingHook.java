package com.mlib.mixin;

import com.mlib.ObfuscationGetter;
import com.mlib.contexts.OnFishingTimeSet;
import net.minecraft.world.entity.projectile.FishingHook;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin( FishingHook.class )
public abstract class MixinFishingHook {
	private static final ObfuscationGetter.Field< FishingHook, Integer > TIME_UNTIL_LURED = new ObfuscationGetter.Field<>( FishingHook.class, "f_37090_" );

	@Shadow( aliases = { "this$0" } )
	@Redirect( method = "catchingFish (Lnet/minecraft/core/BlockPos;)V", at = @At( value = "FIELD", target = "Lnet/minecraft/world/entity/projectile/FishingHook;timeUntilLured:I", opcode = Opcodes.PUTFIELD, ordinal = 3 ) )
	private void catchingFish( FishingHook hook, int timeUntilLured ) {
		if( timeUntilLured > 0 ) {
			TIME_UNTIL_LURED.set( hook, OnFishingTimeSet.dispatch( hook, timeUntilLured ).getTicks() );
		}
	}
}
