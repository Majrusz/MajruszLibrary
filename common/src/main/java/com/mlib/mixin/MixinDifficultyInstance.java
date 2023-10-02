package com.mlib.mixin;

import com.mlib.contexts.OnClampedRegionalDifficultyGet;
import net.minecraft.world.DifficultyInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin( DifficultyInstance.class )
public abstract class MixinDifficultyInstance {
	@Inject(
		at = @At( "RETURN" ),
		cancellable = true,
		method = "getSpecialMultiplier ()F"
	)
	private void getSpecialMultiplier( CallbackInfoReturnable< Float > callback ) {
		DifficultyInstance difficultyInstance = ( DifficultyInstance )( Object )this;

		callback.setReturnValue( OnClampedRegionalDifficultyGet.dispatch( difficultyInstance.getDifficulty(), callback.getReturnValue() ).getClamped() );
	}
}
