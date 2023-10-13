package com.mlib.contexts;

import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

import java.util.function.Function;

public class OnParticlesRegisteredPlatformFabric implements OnParticlesRegistered.IPlatform {
	@Override
	public < Type extends ParticleOptions > void register( ParticleEngine engine, ParticleType< Type > type,
		Function< SpriteSet, ParticleProvider< Type > > factory
	) {
		engine.register( type, factory::apply );
	}
}
