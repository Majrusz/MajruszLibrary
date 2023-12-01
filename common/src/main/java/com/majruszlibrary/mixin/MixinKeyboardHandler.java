package com.majruszlibrary.mixin;

import com.majruszlibrary.events.OnKeyStateChanged;
import com.majruszlibrary.events.base.Events;
import com.majruszlibrary.platform.Side;
import net.minecraft.client.KeyboardHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin( KeyboardHandler.class )
public abstract class MixinKeyboardHandler {
	@Inject(
		at = @At( "RETURN" ),
		method = "keyPress (JIIII)V"
	)
	private void keyPress( long windowId, int key, int scanCode, int action, int modifiers, CallbackInfo callback ) {
		if( Side.getMinecraft().getWindow().getWindow() == windowId ) {
			Events.dispatch( new OnKeyStateChanged( key, scanCode, action, modifiers ) );
		}
	}
}
