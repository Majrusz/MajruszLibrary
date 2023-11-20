package com.majruszlibrary.mixin;

import com.majruszlibrary.contexts.OnAnimalTamed;
import com.majruszlibrary.contexts.base.Contexts;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin( AbstractHorse.class )
public abstract class MixinAbstractHorse {
	@Inject(
		at = @At( "RETURN" ),
		method = "tameWithName (Lnet/minecraft/world/entity/player/Player;)Z"
	)
	private void tameWithName( Player player, CallbackInfoReturnable< Boolean > callback ) {
		Contexts.dispatch( new OnAnimalTamed( ( AbstractHorse )( Object )this, player ) );
	}
}
