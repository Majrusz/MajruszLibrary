package com.mlib.effects;

import com.mlib.Random;
import com.mlib.math.VectorHelper;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ParticleHandler {
	public static final ParticleHandler AWARD = new ParticleHandler( ParticleTypes.HAPPY_VILLAGER, offset( 0.25f ), speed( 0.1f ) );
	public static final ParticleHandler ENCHANTED_HIT = new ParticleHandler( ParticleTypes.ENCHANTED_HIT, offset( 0.25f ), speed( 0.1f ) );
	public static final ParticleHandler PORTAL = new ParticleHandler( ParticleTypes.PORTAL, offset( 0.5f ), ()->Random.nextFloat( -1.0f, 0.0f ) );
	public static final ParticleHandler SMELT = new ParticleHandler( ParticleTypes.FLAME, offset( 0.1f ), speed( 0.01f ) );
	public static final ParticleHandler SMOKE = new ParticleHandler( ParticleTypes.SMOKE, offset( 0.25f ), speed( 0.01f ) );
	public static final ParticleHandler SOUL = new ParticleHandler( ParticleTypes.SOUL, offset( 0.5f ), speed( 0.02f ) );
	public static final ParticleHandler WITCH = new ParticleHandler( ParticleTypes.WITCH, offset( 0.25f ), speed( 0.01f ) );

	final Supplier< SimpleParticleType > particleType;
	final Supplier< Vec3 > offsetProvider;
	final Supplier< Float > speedProvider;

	public static Supplier< Vec3 > offset( float value ) {
		return ()->new Vec3( value, value, value );
	}

	public static Supplier< Float > speed( float value ) {
		return ()->value;
	}

	public ParticleHandler( RegistryObject< ? extends SimpleParticleType > particleType, Supplier< Vec3 > offsetProvider,
		Supplier< Float > speedProvider
	) {
		this.particleType = particleType::get;
		this.offsetProvider = offsetProvider;
		this.speedProvider = speedProvider;
	}

	public ParticleHandler( SimpleParticleType particleType, Supplier< Vec3 > offsetProvider, Supplier< Float > speedProvider ) {
		this.particleType = ()->particleType;
		this.offsetProvider = offsetProvider;
		this.speedProvider = speedProvider;
	}

	public void spawn( ServerLevel level, Vec3 position, int amountOfParticles, Supplier< Vec3 > offsetProvider,
		Supplier< Float > speedProvider
	) {
		Vec3 offset = offsetProvider.get();
		level.sendParticles( this.particleType.get(), position.x, position.y, position.z, amountOfParticles, offset.x, offset.y, offset.z, speedProvider.get() );
	}

	public void spawn( ServerLevel level, Vec3 position, int amountOfParticles, Supplier< Vec3 > offsetProvider ) {
		this.spawn( level, position, amountOfParticles, offsetProvider, this.speedProvider );
	}

	public void spawn( ServerLevel level, Vec3 position, int amountOfParticles ) {
		this.spawn( level, position, amountOfParticles, this.offsetProvider, this.speedProvider );
	}

	public void spawnLine( ServerLevel level, Vec3 from, Vec3 to, int particlesPerBlock, Supplier< Vec3 > offsetProvider,
		Supplier< Float > speedProvider
	) {
		Vec3 difference = VectorHelper.subtract( to, from );
		int amountOfParticles = ( int )Math.ceil( from.distanceTo( to ) * particlesPerBlock );
		for( int i = 0; i <= amountOfParticles; i++ ) {
			Vec3 step = VectorHelper.add( from, VectorHelper.multiply( difference, ( float )( i ) / amountOfParticles ) );
			this.spawn( level, step, 1, offsetProvider, speedProvider );
		}
	}

	public void spawnLine( ServerLevel level, Vec3 from, Vec3 to, int particlesPerBlock, Supplier< Vec3 > offsetProvider ) {
		this.spawnLine( level, from, to, particlesPerBlock, offsetProvider, speed( 0.0f ) );
	}

	public void spawnLine( ServerLevel level, Vec3 from, Vec3 to, int particlesPerBlock ) {
		this.spawnLine( level, from, to, particlesPerBlock, offset( 0.0f ), speed( 0.0f ) );
	}
}
