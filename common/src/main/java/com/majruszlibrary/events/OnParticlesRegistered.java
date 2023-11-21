package com.majruszlibrary.events;

import com.majruszlibrary.events.base.Event;
import com.majruszlibrary.events.base.Events;
import com.majruszlibrary.platform.Services;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

import java.util.function.Consumer;
import java.util.function.Function;

public class OnParticlesRegistered {
	private static final IPlatform PLATFORM = Services.load( IPlatform.class );
	public final ParticleEngine engine;

	public static Event< OnParticlesRegistered > listen( Consumer< OnParticlesRegistered > consumer ) {
		return Events.get( OnParticlesRegistered.class ).add( consumer );
	}

	public OnParticlesRegistered( ParticleEngine engine ) {
		this.engine = engine;
	}

	public < Type extends ParticleOptions > void register( ParticleType< ? > type, Function< SpriteSet, ParticleProvider< Type > > factory ) {
		PLATFORM.register( this.engine, ( ParticleType< Type > )type, factory );
	}

	public interface IPlatform {
		< Type extends ParticleOptions > void register( ParticleEngine engine, ParticleType< Type > type,
			Function< SpriteSet, ParticleProvider< Type > > factory
		);
	}
}
