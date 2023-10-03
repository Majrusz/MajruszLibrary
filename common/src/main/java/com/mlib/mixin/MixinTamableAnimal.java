package com.mlib.mixin;

import com.mlib.contexts.OnAnimalTamed;
import com.mlib.contexts.base.Contexts;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin( TamableAnimal.class )
public abstract class MixinTamableAnimal {
	@Inject(
		at = @At( "RETURN" ),
		method = "tame (Lnet/minecraft/world/entity/player/Player;)V"
	)
	public void tame( Player player, CallbackInfo callback ) {
		Contexts.dispatch( new OnAnimalTamed( ( TamableAnimal )( Object )this, player ) );
	}
}
