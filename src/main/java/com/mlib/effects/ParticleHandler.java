package com.mlib.effects;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;

import java.util.function.Supplier;

public class ParticleHandler {
	public static final ParticleHandler AWARD = new ParticleHandler( ParticleTypes.HAPPY_VILLAGER, new Vec3( 0.1, 0.1, 0.1 ), ()->0.1f );
	public static final ParticleHandler SMELT = new ParticleHandler( ParticleTypes.FLAME, new Vec3( 0.1, 0.1, 0.1 ), ()->0.01f );

	final SimpleParticleType particleType;
	final Vec3 offset;
	final Supplier< Float > speedProvider;

	public ParticleHandler( SimpleParticleType particleType, Vec3 offset, Supplier< Float > speedProvider ) {
		this.particleType = particleType;
		this.offset = offset;
		this.speedProvider = speedProvider;
	}

	public void spawn( ServerLevel level, Vec3 position, int amountOfParticles ) {
		level.sendParticles( this.particleType, position.x, position.y, position.z, amountOfParticles, this.offset.x, this.offset.y, this.offset.z, this.speedProvider.get() );
	}
}
