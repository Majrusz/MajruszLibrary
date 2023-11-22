package com.mlib.registry;

import com.mlib.annotation.Dist;
import com.mlib.annotation.OnlyIn;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

import java.util.function.Function;

public class Custom {
	@OnlyIn( Dist.CLIENT )
	public interface Particles {
		< Type extends ParticleOptions > void register( ParticleType< Type > type, Function< SpriteSet, ParticleProvider< Type > > factory );
	}
}
