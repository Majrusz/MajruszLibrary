package com.mlib.emitter;

import com.mlib.math.AnyPos;
import com.mlib.math.Random;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ParticleEmitter {
	static final Properties DEFAULT_PROPERTIES = new Properties( ParticleEmitter.offset( 0.25f ), ParticleEmitter.speed( 0.025f, 0.1f ) );
	static final Map< Object, Properties > PROPERTIES = new HashMap<>();
	private final SimpleParticleType type;
	private Supplier< Vec3 > position = ()->Vec3.ZERO;
	private Supplier< Vec3 > offset;
	private Supplier< Float > speed;
	private int count = 1;
	private float countMultiplier = 1.0f;

	static {
		ParticleEmitter.setDefault( ParticleTypes.PORTAL, new Properties( ParticleEmitter.offset( 0.5f ), ParticleEmitter.speed( -1.0f, 0.0f ) ) );
		ParticleEmitter.setDefault( ParticleTypes.FLAME, new Properties( ParticleEmitter.offset( 0.15f ), ParticleEmitter.speed( 0.0025f, 0.01f ) ) );
		ParticleEmitter.setDefault( ParticleTypes.SMOKE, new Properties( ParticleEmitter.offset( 0.25f ), ParticleEmitter.speed( 0.0025f, 0.01f ) ) );
		ParticleEmitter.setDefault( ParticleTypes.SONIC_BOOM, new Properties( ()->Vec3.ZERO, ()->0.0f ) );
		ParticleEmitter.setDefault( ParticleTypes.SOUL, new Properties( ParticleEmitter.offset( 0.5f ), ParticleEmitter.speed( 0.005f, 0.02f ) ) );
		ParticleEmitter.setDefault( ParticleTypes.WITCH, new Properties( ParticleEmitter.offset( 0.5f ), ParticleEmitter.speed( 0.0025f, 0.01f ) ) );
	}

	public static void setDefault( SimpleParticleType type, Properties properties ) {
		PROPERTIES.put( type, properties );
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

	public void emit( ServerLevel level ) {
		this.emit( level, this.position.get(), this.offset.get(), Math.round( this.count * this.countMultiplier ) );
	}

	public void emitLine( ServerLevel level, Vec3 end ) {
		Vec3 start = this.position.get();
		int count = Math.round( this.count * this.countMultiplier );
		AnyPos direction = AnyPos.from( end ).sub( start );
		if( count == 1 ) {
			this.emit( level, AnyPos.from( start ).add( direction.div( 2 ) ).vec3(), this.offset.get(), 1 );
			return;
		}

		for( int idx = 0; idx < count; ++idx ) {
			this.emit( level, AnyPos.from( start ).add( direction.mul( idx / ( count - 1.0f ) ) ).vec3(), this.offset.get(), 1 );
		}
	}

	public ParticleEmitter position( Supplier< Vec3 > position ) {
		this.position = position;

		return this;
	}

	public ParticleEmitter position( Vec3 position ) {
		return this.position( ()->position );
	}

	public ParticleEmitter offset( Supplier< Vec3 > offset ) {
		this.offset = offset;

		return this;
	}

	public ParticleEmitter offset( Vec3 offset ) {
		return this.offset( ()->offset );
	}

	public ParticleEmitter speed( Supplier< Float > speed ) {
		this.speed = speed;

		return this;
	}

	public ParticleEmitter speed( float speed ) {
		return this.speed( ()->speed );
	}

	public ParticleEmitter count( int count ) {
		this.count = count;

		return this;
	}

	public ParticleEmitter sizeBased( Entity entity ) {
		float width = entity.getBbWidth();
		float height = entity.getBbHeight();
		this.position = ()->AnyPos.from( entity.position() ).add( 0.0, 0.5 * height, 0.0 ).vec3();
		this.offset = ()->AnyPos.from( width, height, width ).mul( 0.5 ).vec3();
		this.countMultiplier = Math.round( ( 1.0f + width + height ) );

		return this;
	}

	private ParticleEmitter( SimpleParticleType type, Properties properties ) {
		this.type = type;
		this.offset = properties.offset;
		this.speed = properties.speed;
	}

	private void emit( ServerLevel level, Vec3 position, Vec3 offset, int count ) {
		level.sendParticles( this.type, position.x, position.y, position.z, count, offset.x, offset.y, offset.z, this.speed.get() );
	}

	public record Properties( Supplier< Vec3 > offset, Supplier< Float > speed ) {}
}
