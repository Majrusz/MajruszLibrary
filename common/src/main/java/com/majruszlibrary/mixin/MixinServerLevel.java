package com.majruszlibrary.mixin;

import com.majruszlibrary.contexts.OnBlockPlaced;
import com.majruszlibrary.contexts.base.Contexts;
import com.majruszlibrary.mixininterfaces.IMixinExplosion;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin( ServerLevel.class )
public abstract class MixinServerLevel {
	@Inject(
		at = @At( "RETURN" ),
		method = "sendBlockUpdated (Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/state/BlockState;I)V"
	)
	private void sendBlockUpdated( BlockPos position, BlockState previousState, BlockState newState, int p_46615_, CallbackInfo callback ) {
		Contexts.dispatch( new OnBlockPlaced( ( ServerLevel )( Object )this, position, newState ) );
	}

	@Inject(
		at = @At(
			target = "Lnet/minecraft/world/level/Explosion;interactsWithBlocks ()Z",
			value = "INVOKE"
		),
		cancellable = true,
		locals = LocalCapture.CAPTURE_FAILHARD,
		method = "explode (Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/damagesource/DamageSource;Lnet/minecraft/world/level/ExplosionDamageCalculator;DDDFZLnet/minecraft/world/level/Level$ExplosionInteraction;)Lnet/minecraft/world/level/Explosion;"
	)
	private void explode( Entity entity, DamageSource source, ExplosionDamageCalculator calculator, double x, double y, double z, float radius,
		boolean spawnsFire, Level.ExplosionInteraction interaction, CallbackInfoReturnable< Explosion > callback, Explosion explosion
	) {
		IMixinExplosion mixinExplosion = ( IMixinExplosion )explosion;
		radius = mixinExplosion.majruszlibrary$getRadius();
		spawnsFire = mixinExplosion.majruszlibrary$getSpawnsFire();
		if( mixinExplosion.majruszlibrary$isExplosionCancelled() ) {
			callback.setReturnValue( explosion );
		}
	}
}
