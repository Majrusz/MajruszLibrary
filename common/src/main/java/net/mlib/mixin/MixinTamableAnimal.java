package net.mlib.mixin;

import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.mlib.contexts.OnAnimalTamed;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin( TamableAnimal.class )
public abstract class MixinTamableAnimal {
	@Shadow( aliases = { "this$0" } )
	@Inject(
		at = @At( "RETURN" ),
		method = "tame (Lnet/minecraft/world/entity/player/Player;)V"
	)
	public void tame( Player player, CallbackInfo callback ) {
		OnAnimalTamed.dispatch( ( TamableAnimal )( Object )this, player );
	}
}
