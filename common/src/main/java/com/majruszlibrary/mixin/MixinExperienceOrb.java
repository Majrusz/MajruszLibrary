package com.majruszlibrary.mixin;

import com.majruszlibrary.contexts.OnExpOrbPickedUp;
import com.majruszlibrary.contexts.base.Contexts;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin( ExperienceOrb.class )
public abstract class MixinExperienceOrb {
	private @Shadow int value;

	@Inject(
		at = @At(
			target = "Lnet/minecraft/world/entity/player/Player;take (Lnet/minecraft/world/entity/Entity;I)V",
			value = "INVOKE"
		),
		method = "playerTouch (Lnet/minecraft/world/entity/player/Player;)V"
	)
	private void playerTouch( Player player, CallbackInfo callback ) {
		this.value = Contexts.dispatch( new OnExpOrbPickedUp( player, ( ExperienceOrb )( Object )this, this.value ) ).getExperience();
	}
}
