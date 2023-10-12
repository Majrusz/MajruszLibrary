package com.mlib.mixin;

import com.mlib.contexts.OnKeyPressed;
import com.mlib.contexts.base.Contexts;
import com.mlib.platform.Platform;
import net.minecraft.client.KeyboardHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin( KeyboardHandler.class )
public abstract class MixinKeyboardHandler {
	@Inject(
		at = @At( "TAIL" ),
		method = "keyPress (JIIII)V"
	)
	private void keyPress( long windowId, int key, int scanCode, int action, int modifiers, CallbackInfo callback ) {
		if( Platform.getMinecraft().getWindow().getWindow() == windowId ) {
			Contexts.dispatch( new OnKeyPressed( key, scanCode, action, modifiers ) );
		}
	}
}
