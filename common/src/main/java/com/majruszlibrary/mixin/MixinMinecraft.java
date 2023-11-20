package com.majruszlibrary.mixin;

import com.majruszlibrary.contexts.OnClientTicked;
import com.majruszlibrary.contexts.OnGameInitialized;
import com.majruszlibrary.contexts.base.Contexts;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin( Minecraft.class )
public abstract class MixinMinecraft {
	@Inject(
		at = @At( "HEAD" ),
		method = "onGameLoadFinished ()V"
	)
	private void onGameLoadFinished( CallbackInfo callback ) {
		Contexts.dispatch( new OnGameInitialized() );
	}

	@Inject(
		at = @At( "TAIL" ),
		method = "tick ()V"
	)
	private void tick( CallbackInfo callback ) {
		Contexts.dispatch( new OnClientTicked() );
	}
}
