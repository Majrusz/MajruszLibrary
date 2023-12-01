package com.majruszlibrary.mixin;

import com.majruszlibrary.events.OnItemRenderColorGet;
import com.majruszlibrary.events.base.Events;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin( ItemColors.class )
public abstract class MixinItemColors {
	@Inject(
		at = @At( "RETURN" ),
		cancellable = true,
		method = "getColor (Lnet/minecraft/world/item/ItemStack;I)I"
	)
	private void getColor( ItemStack itemStack, int layerIdx, CallbackInfoReturnable< Integer > callback ) {
		callback.setReturnValue( Events.dispatch( new OnItemRenderColorGet( itemStack, layerIdx, callback.getReturnValue() ) ).getColor() );
	}
}
