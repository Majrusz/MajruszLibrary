package com.mlib.mixin;

import com.mlib.contexts.OnDimensionChanged;
import com.mlib.contexts.OnEntityDied;
import com.mlib.contexts.base.Contexts;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin( ServerPlayer.class )
public abstract class MixinServerPlayer {
	@Inject(
		at = @At( "HEAD" ),
		cancellable = true,
		method = "die (Lnet/minecraft/world/damagesource/DamageSource;)V"
	)
	private void die( DamageSource source, CallbackInfo callback ) {
		if( Contexts.dispatch( new OnEntityDied( source, ( ServerPlayer )( Object )this ) ).isDeathCancelled() ) {
			callback.cancel();
		}
	}

	@Inject(
		at = @At( "HEAD" ),
		method = "triggerDimensionChangeTriggers (Lnet/minecraft/server/level/ServerLevel;)V"
	)
	private void triggerDimensionChangeTriggers( ServerLevel level, CallbackInfo callback ) {
		ServerPlayer player = ( ServerPlayer )( Object )this;

		Contexts.dispatch( new OnDimensionChanged( player, level, player.serverLevel() ) );
	}
}
