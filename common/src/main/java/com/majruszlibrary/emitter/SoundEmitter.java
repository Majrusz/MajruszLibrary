package com.majruszlibrary.emitter;

import com.majruszlibrary.math.Random;
import com.majruszlibrary.registry.Registries;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class SoundEmitter {
	static final Properties DEFAULT_PROPERTIES = new Properties( SoundSource.AMBIENT );
	static final Map< Object, Properties > PROPERTIES = new HashMap<>();
	private final SoundEvent event;
	private SoundSource source;
	private Supplier< Vec3 > position = ()->Vec3.ZERO;
	private Supplier< Float > volume;
	private Supplier< Float > pitch;

	static {
		SoundEmitter.setDefault( SoundEvents.BONE_MEAL_USE, new Properties( SoundSource.PLAYERS ) );
		SoundEmitter.setDefault( SoundEvents.GENERIC_DRINK, new Properties( SoundSource.PLAYERS ) );
		SoundEmitter.setDefault( SoundEvents.ENCHANTMENT_TABLE_USE, new Properties( SoundSource.PLAYERS ) );
		SoundEmitter.setDefault( SoundEvents.ENDERMAN_TELEPORT, new Properties( SoundSource.PLAYERS ) );
		SoundEmitter.setDefault( SoundEvents.WARDEN_HEARTBEAT, new Properties( SoundSource.PLAYERS ) );
		SoundEmitter.setDefault( SoundEvents.ITEM_BREAK, new Properties( SoundSource.PLAYERS ) );
		SoundEmitter.setDefault( SoundEvents.ITEM_PICKUP, new Properties( SoundSource.PLAYERS ) );
	}

	public static void setDefault( SoundEvent event, Properties properties ) {
		PROPERTIES.put( event, properties );
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

	public void emit( Level level ) {
		Vec3 position = this.position.get();
		level.playSound( null, position.x, position.y, position.z, this.event, this.source, this.volume.get(), this.pitch.get() );
	}

	public void send( ServerPlayer player ) {
		Vec3 position = this.position.get();
		player.connection.send( new ClientboundSoundPacket( Registries.SOUND_EVENTS.getHolder( this.event ), this.source, position.x, position.y, position.z, this.volume.get(), this.pitch.get(), Random.nextInt() ) );
	}

	public SoundEmitter source( SoundSource source ) {
		this.source = source;

		return this;
	}

	public SoundEmitter position( Supplier< Vec3 > position ) {
		this.position = position;

		return this;
	}

	public SoundEmitter position( Vec3 position ) {
		return this.position( ()->position );
	}

	public SoundEmitter volume( Supplier< Float > volume ) {
		this.volume = volume;

		return this;
	}

	public SoundEmitter volume( float volume ) {
		return this.volume( ()->volume );
	}

	public SoundEmitter pitch( Supplier< Float > pitch ) {
		this.pitch = pitch;

		return this;
	}

	public SoundEmitter pitch( float pitch ) {
		return this.pitch( ()->pitch );
	}

	private SoundEmitter( SoundEvent event, Properties properties ) {
		this.event = event;
		this.source = properties.source;
		this.volume = properties.volume;
		this.pitch = properties.pitch;
	}

	public record Properties( SoundSource source, Supplier< Float > volume, Supplier< Float > pitch ) {
		public Properties( SoundSource source ) {
			this( source, SoundEmitter.randomized( 0.7f ), SoundEmitter.randomized( 1.0f ) );
		}
	}
}
