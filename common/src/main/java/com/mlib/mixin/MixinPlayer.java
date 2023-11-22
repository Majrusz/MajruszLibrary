package com.mlib.mixin;

import com.mlib.contexts.OnEntityDamaged;
import com.mlib.contexts.OnPlayerInteracted;
import com.mlib.contexts.OnPlayerTicked;
import com.mlib.contexts.OnPlayerWakedUp;
import com.mlib.contexts.base.Contexts;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin( Player.class )
public abstract class MixinPlayer {
	@Inject(
		at = @At(
			target = "Lnet/minecraft/world/damagesource/CombatTracker;recordDamage (Lnet/minecraft/world/damagesource/DamageSource;F)V",
			value = "INVOKE"
		),
		method = "actuallyHurt (Lnet/minecraft/world/damagesource/DamageSource;F)V"
	)
	private void actuallyHurt( DamageSource source, float damage, CallbackInfo callback ) {
		Contexts.dispatch( new OnEntityDamaged( source, ( Player )( Object )this, damage ) );
	}

	@Inject(
		at = @At( "TAIL" ),
		method = "tick ()V"
	)
	private void tick( CallbackInfo callback ) {
		Contexts.dispatch( new OnPlayerTicked( ( Player )( Object )this ) );
	}

	@Inject(
		at = @At(
			target = "Lnet/minecraft/world/entity/player/Player;getItemInHand (Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/item/ItemStack;",
			value = "INVOKE"
		),
		cancellable = true,
		method = "interactOn (Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResult;"
	)
	private void interactOn( Entity entity, InteractionHand hand, CallbackInfoReturnable< InteractionResult > callback ) {
		OnPlayerInteracted data = Contexts.dispatch( new OnPlayerInteracted( ( Player )( Object )this, hand, entity ) );
		if( data.hasResult() ) {
			callback.setReturnValue( data.getResult() );
		}
	}

	@Inject(
		at = @At( "TAIL" ),
		method = "stopSleepInBed (ZZ)V"
	)
	public void stopSleepInBed( boolean $$0, boolean wasSleepStoppedManually, CallbackInfo callback ) {
		Contexts.dispatch( new OnPlayerWakedUp( ( Player )( Object )this, wasSleepStoppedManually ) );
	}
}
