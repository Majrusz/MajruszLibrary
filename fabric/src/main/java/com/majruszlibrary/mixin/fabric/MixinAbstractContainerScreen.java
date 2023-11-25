package com.majruszlibrary.mixin.fabric;

import com.majruszlibrary.events.OnItemInventoryClicked;
import com.majruszlibrary.events.base.Events;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin( AbstractContainerScreen.class )
public abstract class MixinAbstractContainerScreen {
	@Inject(
		at = @At(
			target = "Lnet/minecraft/Util;getMillis ()J",
			value = "INVOKE"
		),
		cancellable = true,
		locals = LocalCapture.CAPTURE_FAILHARD,
		method = "mouseClicked (DDI)Z"
	)
	private void mouseClicked( double x, double y, int action, CallbackInfoReturnable< Boolean > callback, boolean $$0, Slot slot ) {
		if( Events.dispatch( new OnItemInventoryClicked( slot, action ) ).isCancelled() ) {
			callback.setReturnValue( true );
		}
	}
}
