package com.mlib.particles;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;

/** Common functions for particles and sounds. */
public class ParticleHelper {
	/** Spawns flame particles and plays fire charge sound. */
	public static void spawnSmeltParticles( ServerLevel level, Vec3 position, int amountOfParticles ) {
		level.sendParticles( ParticleTypes.FLAME, position.x, position.y, position.z, amountOfParticles, 0.125, 0.125, 0.125, 0.03125 );
		level.playSound( null, position.x, position.y, position.z, SoundEvents.FIRECHARGE_USE, SoundSource.AMBIENT, 0.05f, 0.9f );
	}

	/** Spawns flame particles and plays fire charge sound. */
	public static void spawnAwardParticles( ServerLevel level, Vec3 position, int amountOfParticles, double offset ) {
		level.sendParticles( ParticleTypes.HAPPY_VILLAGER, position.x, position.y, position.z, amountOfParticles, offset, offset, offset, 0.1 );
		level.playSound( null, position.x, position.y, position.z, SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.AMBIENT, 0.05f, 0.9f );
	}
}
