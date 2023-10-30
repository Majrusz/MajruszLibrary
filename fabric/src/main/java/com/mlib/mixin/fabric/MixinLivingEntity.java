package com.mlib.mixin.fabric;

import com.mlib.mixininterfaces.IMixinLivingEntity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin( LivingEntity.class )
public abstract class MixinLivingEntity {
	@ModifyVariable(
		at = @At(
			target = "Lnet/minecraft/world/entity/LivingEntity;hasEffect (Lnet/minecraft/world/effect/MobEffect;)Z",
			value = "INVOKE",
			ordinal = 1
		),
		method = "travel (Lnet/minecraft/world/phys/Vec3;)V",
		ordinal = 1
	)
	private float modifySwimSpeed( float speed ) {
		return speed * ( this instanceof IMixinLivingEntity entity ? entity.getSwimSpeedMultiplier() : 1.0f );
	}

	@ModifyArg(
		method = "jumpInLiquid (Lnet/minecraft/tags/TagKey;)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/phys/Vec3;add (DDD)Lnet/minecraft/world/phys/Vec3;"
		),
		index = 1
	)
	private double jumpInLiquid( double y ) {
		return y * ( this instanceof IMixinLivingEntity entity ? entity.getSwimSpeedMultiplier() : 1.0f );
	}

	@ModifyArg(
		method = "goDownInWater ()V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/phys/Vec3;add (DDD)Lnet/minecraft/world/phys/Vec3;"
		),
		index = 1
	)
	private double goDownInWater( double y ) {
		return y * ( this instanceof IMixinLivingEntity entity ? entity.getSwimSpeedMultiplier() : 1.0f );
	}
}
