package com.majruszlibrary.mixin;

import com.majruszlibrary.events.OnCreativeModeTabIconGet;
import com.majruszlibrary.events.base.Events;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin( CreativeModeTab.class )
public abstract class MixinCreativeModeTab {
	private @Shadow @Final Component displayName;

	@Inject(
		at = @At( "RETURN" ),
		cancellable = true,
		method = "getIconItem ()Lnet/minecraft/world/item/ItemStack;"
	)
	private void getIconItem( CallbackInfoReturnable< ItemStack > callback ) {
		OnCreativeModeTabIconGet data = Events.dispatch( new OnCreativeModeTabIconGet( ( CreativeModeTab )( Object )this, this.displayName, callback.getReturnValue() ) );
		if( !data.icon.isEmpty() ) {
			callback.setReturnValue( data.icon );
		}
	}
}
