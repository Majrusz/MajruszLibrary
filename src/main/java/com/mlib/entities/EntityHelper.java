package com.mlib.entities;

import com.mlib.CommonHelper;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;

/** Class with common code for entities. */
public class EntityHelper {
	/** Simulates cheating death like Totem of Undying does. */
	public static void cheatDeath( LivingEntity entity, float healthRatio, boolean shouldPlayEffects ) {
		entity.setHealth( entity.getMaxHealth() * healthRatio );

		if( shouldPlayEffects && entity.level instanceof ServerLevel ) {
			ServerLevel level = ( ServerLevel )entity.level;

			level.sendParticles( ParticleTypes.TOTEM_OF_UNDYING, entity.getX(), entity.getY( 0.75 ), entity.getZ(), 64, 0.25, 0.5, 0.25, 0.5 );
			level.playSound( null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.TOTEM_USE, SoundSource.AMBIENT, 1.0f, 1.0f );
		}
	}

	/**
	 Returns Player from given entity.
	 Returns null if casting was impossible.
	 */
	@Nullable
	public static Player getPlayerFromEntity( @Nullable Entity entity ) {
		return CommonHelper.castIfPossible( Player.class, entity );
	}

	/** Checks whether given player has Creative Mode enabled. */
	public static boolean isOnCreativeMode( Player player ) {
		return player.getAbilities().instabuild;
	}

	/** Returns given entity's health ratio. */
	public static double getHealthRatio( LivingEntity entity ) {
		return Mth.clamp( entity.getHealth()/entity.getMaxHealth(), 0.0f, 1.0f );
	}

	/** Returns given entity's missing health ratio. */
	public static double getMissingHealthRatio( LivingEntity entity ) {
		return 1.0f - getHealthRatio( entity );
	}
}
