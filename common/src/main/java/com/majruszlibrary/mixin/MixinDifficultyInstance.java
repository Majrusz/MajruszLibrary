package com.majruszlibrary.mixin;

import com.majruszlibrary.contexts.OnClampedRegionalDifficultyGet;
import com.majruszlibrary.contexts.base.Contexts;
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
		OnClampedRegionalDifficultyGet data = Contexts.dispatch( new OnClampedRegionalDifficultyGet( ( ( DifficultyInstance )( Object )this ).getDifficulty(), callback.getReturnValue() ) );

		callback.setReturnValue( data.getClamped() );
	}
}
