package com.mlib.mixin;

import com.mlib.contexts.OnClampedRegionalDifficultyGet;
import net.minecraft.world.DifficultyInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin( DifficultyInstance.class )
public abstract class MixinDifficultyInstance {
	@Shadow( aliases = { "this$0" } )
	@Inject( method = "getSpecialMultiplier ()F", at = @At( "RETURN" ), cancellable = true )
	private void getSpecialMultiplier( CallbackInfoReturnable< Float > callback ) {
		DifficultyInstance difficultyInstance = ( DifficultyInstance )( Object )this;

		callback.setReturnValue( OnClampedRegionalDifficultyGet.dispatch( difficultyInstance.getDifficulty(), callback.getReturnValue() ).getClamped() );
	}
}
