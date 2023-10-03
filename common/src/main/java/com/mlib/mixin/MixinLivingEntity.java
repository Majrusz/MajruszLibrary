package com.mlib.mixin;

import com.mlib.contexts.OnPreDamaged;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin( LivingEntity.class )
public abstract class MixinLivingEntity {
	float mlibLastExtraDamage = 0.0f;

	@Shadow( aliases = { "this$0" } )
	@Inject( method = "hurt (Lnet/minecraft/world/damagesource/DamageSource;F)Z", at = @At( "HEAD" ), cancellable = true )
	private void hurt( DamageSource source, float damage, CallbackInfoReturnable< Boolean > callback ) {
		this.mlibLastExtraDamage = 0.0f;
		LivingEntity entity = ( LivingEntity )( Object )this;
		if( damage == 0.0f || willBeCancelled( source, entity ) ) {
			return;
		}

		OnPreDamaged.Data data = OnPreDamaged.dispatch( source, entity, damage );
		if( data.isDamageCancelled() ) {
			callback.setReturnValue( false );
		} else {
			this.mlibLastExtraDamage = data.extraDamage;
			tryToAddMagicParticles( data );
		}
	}

	@Shadow( aliases = { "this$0" } )
	@ModifyVariable( method = "hurt (Lnet/minecraft/world/damagesource/DamageSource;F)Z", at = @At( value = "HEAD", shift = At.Shift.AFTER ), ordinal = 0 )
	private float addExtraDamage( float damage ) {
		return damage + this.mlibLastExtraDamage;
	}

	private static void tryToAddMagicParticles( OnPreDamaged.Data data ) {
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
