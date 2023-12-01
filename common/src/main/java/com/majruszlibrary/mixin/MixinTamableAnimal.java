package com.majruszlibrary.mixin;

import com.majruszlibrary.events.OnAnimalTamed;
import com.majruszlibrary.events.base.Events;
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
	private void tame( Player player, CallbackInfo callback ) {
		Events.dispatch( new OnAnimalTamed( ( TamableAnimal )( Object )this, player ) );
	}
}
