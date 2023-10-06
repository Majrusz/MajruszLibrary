package com.mlib.mixin;

import com.mlib.contexts.OnBabySpawned;
import com.mlib.contexts.base.Contexts;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin( Animal.class )
public abstract class MixinAnimal {
	@Inject(
		at = @At(
			target = "Lnet/minecraft/server/level/ServerLevel;addFreshEntityWithPassengers (Lnet/minecraft/world/entity/Entity;)V",
			value = "INVOKE"
		),
		locals = LocalCapture.CAPTURE_FAILHARD,
		method = "spawnChildFromBreeding (Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/animal/Animal;)V"
	)
	private void spawnChildFromBreeding( ServerLevel level, Animal parentB, CallbackInfo callback, AgeableMob child ) {
		Animal parentA = ( Animal )( Object )this;
		Player player = parentA.getLoveCause() != null ? parentA.getLoveCause() : parentB.getLoveCause();

		Contexts.dispatch( new OnBabySpawned( parentA, parentB, child, player ) );
	}
}
