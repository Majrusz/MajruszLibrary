package com.mlib.emitter;

import com.mlib.math.Random;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class SoundEmitter {
	static final Properties DEFAULT_PROPERTIES = new Properties( SoundSource.AMBIENT );
	static final Map< Object, Properties > PROPERTIES = new HashMap<>();
	final SoundEvent event;
	SoundSource source;
	Supplier< Float > volume;
	Supplier< Float > pitch;

	static {
		PROPERTIES.put( SoundEvents.BONE_MEAL_USE, new Properties( SoundSource.PLAYERS ) );
		PROPERTIES.put( SoundEvents.GENERIC_DRINK, new Properties( SoundSource.PLAYERS ) );
		PROPERTIES.put( SoundEvents.ENCHANTMENT_TABLE_USE, new Properties( SoundSource.PLAYERS ) );
		PROPERTIES.put( SoundEvents.ENDERMAN_TELEPORT, new Properties( SoundSource.PLAYERS ) );
		PROPERTIES.put( SoundEvents.WARDEN_HEARTBEAT, new Properties( SoundSource.PLAYERS ) );
		PROPERTIES.put( SoundEvents.ITEM_BREAK, new Properties( SoundSource.PLAYERS ) );
		PROPERTIES.put( SoundEvents.ITEM_PICKUP, new Properties( SoundSource.PLAYERS ) );
	}

	public static SoundEmitter of( Supplier< ? extends SoundEvent > event ) {
		return new SoundEmitter( event.get(), PROPERTIES.getOrDefault( event.get(), DEFAULT_PROPERTIES ) );
	}

	public static SoundEmitter of( SoundEvent event ) {
		return SoundEmitter.of( ()->event );
	}

	public static Supplier< Float > randomized( float min, float max ) {
		return ()->Random.nextFloat( min, max );
	}

	public static Supplier< Float > randomized( float value ) {
		return SoundEmitter.randomized( value * 0.8f, value * 1.2f );
	}

	public void emit( ServerLevel level, Vec3 position ) {
		level.playSound( null, position.x, position.y, position.z, this.event, this.source, this.volume.get(), this.pitch.get() );
	}

	public SoundEmitter source( SoundSource source ) {
		this.source = source;

		return this;
	}

	public SoundEmitter volume( Supplier< Float > volume ) {
		this.volume = volume;

		return this;
	}

	public SoundEmitter pitch( Supplier< Float > pitch ) {
		this.pitch = pitch;

		return this;
	}

	private SoundEmitter( SoundEvent event, Properties properties ) {
		this.event = event;
		this.source = properties.source;
		this.volume = properties.volume;
		this.pitch = properties.pitch;
	}

	private record Properties( SoundSource source, Supplier< Float > volume, Supplier< Float > pitch ) {
		public Properties( SoundSource source ) {
			this( source, SoundEmitter.randomized( 0.7f ), SoundEmitter.randomized( 1.0f ) );
		}
	}
}
