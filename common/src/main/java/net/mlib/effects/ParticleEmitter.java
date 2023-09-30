package net.mlib.effects;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;
import net.mlib.math.AnyPos;
import net.mlib.math.Random;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ParticleEmitter {
	static final Properties DEFAULT_PROPERTIES = new Properties( ParticleEmitter.offset( 0.25f ), ParticleEmitter.speed( 0.025f, 0.1f ) );
	static final Map< Object, Properties > PROPERTIES = new HashMap<>();
	final SimpleParticleType type;
	Supplier< Vec3 > offset;
	Supplier< Float > speed;
	int count = 1;

	static {
		PROPERTIES.put( ParticleTypes.PORTAL, new Properties( ParticleEmitter.offset( 0.5f ), ParticleEmitter.speed( -1.0f, 0.0f ) ) );
		PROPERTIES.put( ParticleTypes.FLAME, new Properties( ParticleEmitter.offset( 0.15f ), ParticleEmitter.speed( 0.0025f, 0.01f ) ) );
		PROPERTIES.put( ParticleTypes.SMOKE, new Properties( ParticleEmitter.offset( 0.25f ), ParticleEmitter.speed( 0.0025f, 0.01f ) ) );
		PROPERTIES.put( ParticleTypes.SONIC_BOOM, new Properties( ()->Vec3.ZERO, ()->0.0f ) );
		PROPERTIES.put( ParticleTypes.SOUL, new Properties( ParticleEmitter.offset( 0.5f ), ParticleEmitter.speed( 0.005f, 0.02f ) ) );
		PROPERTIES.put( ParticleTypes.WITCH, new Properties( ParticleEmitter.offset( 0.5f ), ParticleEmitter.speed( 0.0025f, 0.01f ) ) );
	}

	public static ParticleEmitter of( Supplier< ? extends SimpleParticleType > type ) {
		return new ParticleEmitter( type.get(), PROPERTIES.getOrDefault( type.get(), DEFAULT_PROPERTIES ) );
	}

	public static ParticleEmitter of( SimpleParticleType type ) {
		return ParticleEmitter.of( ()->type );
	}

	public static Supplier< Vec3 > offset( float value ) {
		return ()->new Vec3( value, value, value );
	}

	public static Supplier< Float > speed( float min, float max ) {
		return ()->Random.nextFloat( min, max );
	}

	public void emit( ServerLevel level, Vec3 position ) {
		Vec3 offset = this.offset.get();
		level.sendParticles( this.type, position.x, position.y, position.z, this.count, offset.x, offset.y, offset.z, this.speed.get() );
	}

	public void emitLine( ServerLevel level, Vec3 start, Vec3 end ) {
		AnyPos direction = AnyPos.from( end ).sub( start );
		if( this.count == 1 ) {
			this.emit( level, AnyPos.from( start ).add( direction.div( 2 ) ).vec3() );
		}

		for( int idx = 0; idx < this.count; idx++ ) {
			Vec3 offset = this.offset.get();
			Vec3 position = AnyPos.from( start ).add( direction.mul( idx / ( this.count - 1.0f ) ) ).vec3();
			level.sendParticles( this.type, position.x, position.y, position.z, this.count, offset.x, offset.y, offset.z, this.speed.get() );
		}
	}

	public ParticleEmitter offset( Supplier< Vec3 > offset ) {
		this.offset = offset;

		return this;
	}

	public ParticleEmitter speed( Supplier< Float > speed ) {
		this.speed = speed;

		return this;
	}

	public ParticleEmitter count( int count ) {
		this.count = count;

		return this;
	}

	private ParticleEmitter( SimpleParticleType type, Properties properties ) {
		this.type = type;
		this.offset = properties.offset;
		this.speed = properties.speed;
	}

	private record Properties( Supplier< Vec3 > offset, Supplier< Float > speed ) {}
}
