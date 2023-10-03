package com.mlib.mixin;

import com.mlib.contexts.OnBabySpawned;
import com.mlib.contexts.base.Contexts;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin( targets = "net.minecraft.world.entity.animal.Fox$FoxBreedGoal" )
public abstract class MixinFoxBreedGoal implements IMixinBreedGoal {
	@Inject(
		at = @At(
			target = "Lnet/minecraft/server/level/ServerLevel;addFreshEntityWithPassengers (Lnet/minecraft/world/entity/Entity;)V",
			value = "INVOKE"
		),
		locals = LocalCapture.CAPTURE_FAILHARD,
		method = "breed ()V"
	)
	public void breed( CallbackInfo callback, ServerLevel level, Fox fox ) {
		Animal parentA = this.getAnimal();
		Animal parentB = this.getPartner();
		Player player = parentA.getLoveCause() != null ? parentA.getLoveCause() : parentB.getLoveCause();

		Contexts.dispatch( new OnBabySpawned( this.getAnimal(), this.getPartner(), player, fox ) );
	}
}
