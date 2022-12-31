package com.mlib.mixin;

import com.mlib.events.ItemSwingDurationEvent;
import com.mlib.gamemodifiers.contexts.OnPreDamaged;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.common.MinecraftForge;
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
	@Inject( method = "getCurrentSwingDuration ()I", at = @At( "RETURN" ), cancellable = true )
	private void getCurrentSwingDuration( CallbackInfoReturnable< Integer > callback ) {
		LivingEntity livingEntity = ( LivingEntity )( Object )this;
		ItemSwingDurationEvent event = new ItemSwingDurationEvent( livingEntity, callback.getReturnValue() );
		MinecraftForge.EVENT_BUS.post( event );

		callback.setReturnValue( event.getTotalSwingDuration() );
	}

	@Shadow( aliases = { "this$0" } )
	@Inject( method = "hurt (Lnet/minecraft/world/damagesource/DamageSource;F)Z", at = @At( "HEAD" ), cancellable = true )
	private void hurt( DamageSource source, float damage, CallbackInfoReturnable< Boolean > callback ) {
		this.mlibLastExtraDamage = 0.0f;
		OnPreDamaged.Data data = new OnPreDamaged.Data( source, ( LivingEntity )( Object )this, damage );
		if( willBeCancelled( data ) )
			return;

		OnPreDamaged.Context.accept( data );
		if( data.isCancelled ) {
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
			if( EnchantmentHelper.getDamageBonus( player.getMainHandItem(), type ) > 0.0f )
				return;

			if( data.spawnCriticalParticles ) {
				player.crit( data.target );
			}
			if( data.spawnMagicParticles ) {
				player.magicCrit( data.target );
			}
		}
	}

	private static boolean willBeCancelled( OnPreDamaged.Data data ) {
		boolean isInvulnerable = data.target.isInvulnerableTo( data.source );
		boolean isClientSide = data.level == null;
		boolean isDeadOrDying = data.target.isDeadOrDying();
		boolean isFireResistant = data.source.isFire() && data.target.hasEffect( MobEffects.FIRE_RESISTANCE );

		return isInvulnerable || isClientSide || isDeadOrDying || isFireResistant;
	}
}
