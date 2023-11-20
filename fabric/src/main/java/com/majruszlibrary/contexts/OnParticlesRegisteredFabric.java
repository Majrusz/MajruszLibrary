package com.majruszlibrary.contexts;

import com.majruszlibrary.contexts.base.Context;
import com.majruszlibrary.contexts.base.Contexts;
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
