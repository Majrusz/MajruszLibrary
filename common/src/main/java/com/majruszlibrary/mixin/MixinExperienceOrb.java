package com.majruszlibrary.mixin;

import com.majruszlibrary.events.OnExpOrbPickedUp;
import com.majruszlibrary.events.base.Events;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin( ExperienceOrb.class )
public abstract class MixinExperienceOrb {
	@ModifyArg(
		at = @At(
			target = "Lnet/minecraft/world/entity/ExperienceOrb;repairPlayerItems (Lnet/minecraft/world/entity/player/Player;I)I",
			value = "INVOKE"
		),
		index = 1,
		method = "playerTouch (Lnet/minecraft/world/entity/player/Player;)V"
	)
	private int getValue( Player player, int value ) {
		return Events.dispatch( new OnExpOrbPickedUp( player, ( ExperienceOrb )( Object )this, value ) ).getExperience();
	}
}
