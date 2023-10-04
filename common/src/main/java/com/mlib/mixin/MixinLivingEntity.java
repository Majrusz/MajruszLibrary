package com.mlib.mixin;

import com.mlib.contexts.*;
import com.mlib.contexts.base.Contexts;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin( LivingEntity.class )
public abstract class MixinLivingEntity {
	float mlibLastExtraDamage = 0.0f;

	@Inject(
		at = @At( "HEAD" ),
		cancellable = true,
		method = "hurt (Lnet/minecraft/world/damagesource/DamageSource;F)Z"
	)
	private void hurt( DamageSource source, float damage, CallbackInfoReturnable< Boolean > callback ) {
		this.mlibLastExtraDamage = 0.0f;
		LivingEntity entity = ( LivingEntity )( Object )this;
		if( damage == 0.0f || willBeCancelled( source, entity ) ) {
			return;
		}

		OnEntityPreDamaged data = Contexts.dispatch( new OnEntityPreDamaged( source, entity, damage ) );
		if( data.isDamageCancelled() ) {
			callback.setReturnValue( false );
		} else {
			this.mlibLastExtraDamage = data.extraDamage;
			tryToAddMagicParticles( data );
		}
	}

	@ModifyVariable(
		at = @At(
			shift = At.Shift.AFTER,
			value = "HEAD"
		),
		method = "hurt (Lnet/minecraft/world/damagesource/DamageSource;F)Z",
		ordinal = 0
	)
	private float addExtraDamage( float damage ) {
		return damage + this.mlibLastExtraDamage;
	}

	@Inject(
		at = @At(
			target = "Lnet/minecraft/world/damagesource/CombatTracker;recordDamage (Lnet/minecraft/world/damagesource/DamageSource;F)V",
			value = "INVOKE"
		),
		method = "actuallyHurt (Lnet/minecraft/world/damagesource/DamageSource;F)V"
	)
	private void actuallyHurt( DamageSource source, float damage, CallbackInfo callback ) {
		Contexts.dispatch( new OnEntityDamaged( source, ( LivingEntity )( Object )this, damage ) );
	}

	@Inject(
		at = @At( "HEAD" ),
		cancellable = true,
		method = "die (Lnet/minecraft/world/damagesource/DamageSource;)V"
	)
	private void die( DamageSource source, CallbackInfo callback ) {
		if( Contexts.dispatch( new OnEntityDied( source, ( LivingEntity )( Object )this ) ).isDeathCancelled() ) {
			callback.cancel();
		}
	}

	@Inject(
		at = @At( "HEAD" ),
		cancellable = true,
		method = "canBeAffected (Lnet/minecraft/world/effect/MobEffectInstance;)Z"
	)
	private void canBeAffected( MobEffectInstance effect, CallbackInfoReturnable< Boolean > callback ) {
		if( Contexts.dispatch( new OnEntityEffectCheck( effect, ( LivingEntity )( Object )this ) ).isEffectCancelled() ) {
			callback.setReturnValue( false );
		}
	}

	@Inject(
		at = @At( "TAIL" ),
		method = "tick ()V"
	)
	private void tick( CallbackInfo callback ) {
		Contexts.dispatch( new OnEntityTicked( ( LivingEntity )( Object )this ) );
	}

	private static void tryToAddMagicParticles( OnEntityPreDamaged data ) {
		if( data.attacker instanceof Player player ) {
			MobType type = data.source.getEntity() instanceof LivingEntity entity ? entity.getMobType() : MobType.UNDEFINED;
			if( EnchantmentHelper.getDamageBonus( player.getMainHandItem(), type ) > 0.0f ) {
				return;
			}

			if( data.spawnCriticalParticles ) {
				player.crit( data.target );
			}
			if( data.spawnMagicParticles ) {
				player.magicCrit( data.target );
			}
		}
	}

	private static boolean willBeCancelled( DamageSource source, LivingEntity target ) {
		boolean isInvulnerable = target.isInvulnerableTo( source );
		boolean isClientSide = !( target.level() instanceof ServerLevel );
		boolean isDeadOrDying = target.isDeadOrDying();
		boolean isFireResistant = source.is( DamageTypeTags.IS_FIRE ) && target.hasEffect( MobEffects.FIRE_RESISTANCE );

		return isInvulnerable || isClientSide || isDeadOrDying || isFireResistant;
	}
}