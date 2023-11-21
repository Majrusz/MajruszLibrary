package com.majruszlibrary.events;

import com.majruszlibrary.events.base.Event;
import com.majruszlibrary.events.base.Events;
import net.minecraft.client.particle.ParticleEngine;

import java.util.function.Consumer;

public class OnParticlesRegisteredFabric {
	public final ParticleEngine engine;

	public static Event< OnParticlesRegisteredFabric > listen( Consumer< OnParticlesRegisteredFabric > consumer ) {
		return Events.get( OnParticlesRegisteredFabric.class ).add( consumer );
	}

	public OnParticlesRegisteredFabric( ParticleEngine engine ) {
		this.engine = engine;
	}
}
