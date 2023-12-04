package com.majruszlibrary.mixin;

import com.majruszlibrary.events.OnItemRenderColorsGet;
import com.majruszlibrary.events.base.Events;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin( value = ItemColors.class, priority = 1100 )
public abstract class MixinItemColors {
	@Inject(
		at = @At( "RETURN" ),
		cancellable = true,
		method = "getColor (Lnet/minecraft/world/item/ItemStack;I)I"
	)
	private void getColor( ItemStack itemStack, int layerIdx, CallbackInfoReturnable< Integer > callback ) {
		OnItemRenderColorsGet data = Events.dispatch( new OnItemRenderColorsGet( itemStack ) );
		if( data.hasColorsDefined() ) {
			callback.setReturnValue( data.toItemColor().getColor( itemStack, layerIdx ) );
		}
	}

	@Dynamic( "Sodium compatibility" )
	@Inject(
		at = @At( "RETURN" ),
		cancellable = true,
		method = "sodium$getColorProvider",
		remap = false,
		require = 0
	)
	private void getColorProvider( ItemStack itemStack, CallbackInfoReturnable< ItemColor > callback ) {
		OnItemRenderColorsGet data = Events.dispatch( new OnItemRenderColorsGet( itemStack ) );
		if( data.hasColorsDefined() ) {
			callback.setReturnValue( data.toItemColor() );
		}
	}
}
