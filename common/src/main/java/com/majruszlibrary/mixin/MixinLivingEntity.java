package com.majruszlibrary.mixin;

import com.majruszlibrary.events.*;
import com.majruszlibrary.events.base.Events;
import com.majruszlibrary.mixininterfaces.IMixinLivingEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Map;

@Mixin( LivingEntity.class )
public abstract class MixinLivingEntity implements IMixinLivingEntity {
	private @Shadow int useItemRemaining;
	float majruszlibrary$lastDamage = 0.0f;
	float majruszlibrary$swimSpeedMultiplier = 1.0f;

	@Override
	public float majruszlibrary$getSwimSpeedMultiplier() {
		return this.majruszlibrary$swimSpeedMultiplier;
	}

	@Inject(
		at = @At( "HEAD" ),
		cancellable = true,
		method = "hurt (Lnet/minecraft/world/damagesource/DamageSource;F)Z"
	)
	private void hurt( DamageSource source, float damage, CallbackInfoReturnable< Boolean > callback ) {
		this.majruszlibrary$lastDamage = damage;
		LivingEntity entity = ( LivingEntity )( Object )this;
		if( majruszlibrary$willBeCancelled( source, entity ) ) {
			return;
		}
		if( damage == 0.0f || entity.isDamageSourceBlocked( source ) ) {
			Events.dispatch( new OnEntityDamageBlocked( source, entity ) );
			return;
		}

		OnEntityPreDamaged data = Events.dispatch( new OnEntityPreDamaged( source, entity, damage ) );
		if( data.isDamageCancelled() ) {
			data.target.invulnerableTime = 20;
			callback.setReturnValue( false );
		} else {
			this.majruszlibrary$lastDamage = data.damage;
			majruszlibrary$tryToAddMagicParticles( data );
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
	private float replacePreDamage( float damage ) {
		return this.majruszlibrary$lastDamage;
	}

	@Inject(
		at = @At(
			target = "Lnet/minecraft/world/damagesource/CombatTracker;recordDamage (Lnet/minecraft/world/damagesource/DamageSource;FF)V",
			shift = At.Shift.BEFORE,
			value = "INVOKE"
		),
		method = "actuallyHurt (Lnet/minecraft/world/damagesource/DamageSource;F)V"
	)
	private void actuallyHurt( DamageSource source, float damage, CallbackInfo callback ) {
		Events.dispatch( new OnEntityDamaged( source, ( LivingEntity )( Object )this, damage ) );
	}

	@Inject(
		at = @At( "HEAD" ),
		cancellable = true,
		method = "die (Lnet/minecraft/world/damagesource/DamageSource;)V"
	)
	private void die( DamageSource source, CallbackInfo callback ) {
		if( Events.dispatch( new OnEntityDied( source, ( LivingEntity )( Object )this ) ).isDeathCancelled() ) {
			callback.cancel();
		}
	}

	@Inject(
		at = @At( "HEAD" ),
		cancellable = true,
		method = "canBeAffected (Lnet/minecraft/world/effect/MobEffectInstance;)Z"
	)
	private void canBeAffected( MobEffectInstance effect, CallbackInfoReturnable< Boolean > callback ) {
		if( Events.dispatch( new OnEntityEffectCheck( effect, ( LivingEntity )( Object )this ) ).isEffectCancelled() ) {
			callback.setReturnValue( false );
		}
	}

	@Inject(
		at = @At( "TAIL" ),
		method = "tick ()V"
	)
	private void tick( CallbackInfo callback ) {
		Events.dispatch( new OnEntityTicked( ( LivingEntity )( Object )this ) );

		this.majruszlibrary$swimSpeedMultiplier = Events.dispatch( new OnEntitySwimSpeedMultiplierGet( ( LivingEntity )( Object )this, 1.0f ) ).getMultiplier();
	}

	@Inject(
		at = @At(
			target = "Lnet/minecraft/world/item/ItemStack;matches (Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z",
			value = "INVOKE"
		),
		locals = LocalCapture.CAPTURE_FAILHARD,
		method = "collectEquipmentChanges ()Ljava/util/Map;"
	)
	private void collectEquipmentChanges( CallbackInfoReturnable< Map< EquipmentSlot, ItemStack > > callback, Map< EquipmentSlot, ItemStack > map,
		EquipmentSlot[] slots, int slotsCount, int idx, EquipmentSlot slot, ItemStack from, ItemStack to
	) {
		if( !ItemStack.matches( from, to ) ) {
			Events.dispatch( new OnItemEquipped( ( LivingEntity )( Object )this, slot, from, to ) );
		}
	}

	@Inject(
		at = @At( "RETURN" ),
		cancellable = true,
		method = "getCurrentSwingDuration ()I"
	)
	private void getCurrentSwingDuration( CallbackInfoReturnable< Integer > callback ) {
		callback.setReturnValue( Events.dispatch( new OnItemSwingDurationGet( ( LivingEntity )( Object )this, callback.getReturnValue() ) )
			.getSwingDuration() );
	}

	@Inject(
		at = @At( "HEAD" ),
		method = "updateUsingItem (Lnet/minecraft/world/item/ItemStack;)V"
	)
	private void updateUsingItem( ItemStack itemStack, CallbackInfo callback ) {
		OnItemUseTicked data = Events.dispatch( new OnItemUseTicked( ( LivingEntity )( Object )this, itemStack, itemStack.getUseDuration(), this.useItemRemaining ) );

		this.useItemRemaining = data.getDuration();
	}

	@Inject(
		at = @At( "HEAD" ),
		method = "dropAllDeathLoot (Lnet/minecraft/world/damagesource/DamageSource;)V"
	)
	private void dropAllDeathLoot( DamageSource source, CallbackInfo callback ) {
		OnLootingLevelGet.Cache.SOURCE = source;
	}

	@Inject(
		at = @At( "HEAD" ),
		method = "eat (Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/item/ItemStack;"
	)
	private void eat( Level level, ItemStack itemStack, CallbackInfoReturnable< ItemStack > callback ) {
		Events.dispatch( new OnItemEaten( ( LivingEntity )( Object )this, itemStack ) );
	}

	private static void majruszlibrary$tryToAddMagicParticles( OnEntityPreDamaged data ) {
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

	private static boolean majruszlibrary$willBeCancelled( DamageSource source, LivingEntity target ) {
		boolean isInvulnerable = target.isInvulnerableTo( source );
		boolean isClientSide = !( target.getLevel() instanceof ServerLevel );
		boolean isDeadOrDying = target.isDeadOrDying();
		boolean isFireResistant = source.isFire() && target.hasEffect( MobEffects.FIRE_RESISTANCE );

		return isInvulnerable || isClientSide || isDeadOrDying || isFireResistant;
	}
}
