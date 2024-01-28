package com.majruszlibrary.mixin.fabric;

import com.majruszlibrary.events.OnGameInitialized;
import com.majruszlibrary.events.base.Events;
import net.minecraft.client.ResourceLoadStateTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin( ResourceLoadStateTracker.class )
public abstract class MixinResourceLoadStateTracker {
	@Inject(
		at = @At( "TAIL" ),
		method = "finishReload ()V"
	)
	private void finishReload( CallbackInfo callback ) {
		Events.dispatch( new OnGameInitialized() );
	}
}
