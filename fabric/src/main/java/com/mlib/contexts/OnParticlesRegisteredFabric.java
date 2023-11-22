package com.mlib.contexts;

import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;
import net.minecraft.client.particle.ParticleEngine;

import java.util.function.Consumer;

public class OnParticlesRegisteredFabric {
	public final ParticleEngine engine;

	public static Context< OnParticlesRegisteredFabric > listen( Consumer< OnParticlesRegisteredFabric > consumer ) {
		return Contexts.get( OnParticlesRegisteredFabric.class ).add( consumer );
	}

	public OnParticlesRegisteredFabric( ParticleEngine engine ) {
		this.engine = engine;
	}
}
