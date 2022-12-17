package com.mlib.effects;

import com.mlib.Random;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;

import java.util.function.Supplier;

public class SoundHandler {
	public static final Supplier< Float > DEFAULT_VOLUME = randomized( 0.7f );
	public static final Supplier< Float > DEFAULT_PITCH = randomized( 1.0f );
	public static final SoundHandler AWARD = new SoundHandler( SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.AMBIENT );
	public static final SoundHandler BONE_MEAL = new SoundHandler( SoundEvents.BONE_MEAL_USE, SoundSource.AMBIENT );
	public static final SoundHandler DRINK = new SoundHandler( SoundEvents.GENERIC_DRINK, SoundSource.PLAYERS );
	public static final SoundHandler ENCHANT = new SoundHandler( SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS );
	public static final SoundHandler ENDERMAN_TELEPORT = new SoundHandler( SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS );
	public static final SoundHandler FIRE_EXTINGUISH = new SoundHandler( SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.AMBIENT );
	public static final SoundHandler ITEM_BREAK = new SoundHandler( SoundEvents.ITEM_BREAK, SoundSource.PLAYERS );
	public static final SoundHandler ITEM_PICKUP = new SoundHandler( SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS );
	public static final SoundHandler SMELT = new SoundHandler( SoundEvents.FIRECHARGE_USE, SoundSource.AMBIENT );

	final SoundEvent event;
	final SoundSource source;
	final Supplier< Float > volumeProvider;
	final Supplier< Float > pitchProvider;

	public static Supplier< Float > randomized( float value ) {
		return ()->value * Random.nextFloat( 0.8f, 1.2f );
	}

	public SoundHandler( SoundEvent event, SoundSource source, Supplier< Float > volumeProvider, Supplier< Float > pitchProvider ) {
		this.event = event;
		this.source = source;
		this.volumeProvider = volumeProvider;
		this.pitchProvider = pitchProvider;
	}

	public SoundHandler( SoundEvent event, SoundSource source, Supplier< Float > volumeProvider ) {
		this( event, source, volumeProvider, DEFAULT_PITCH );
	}

	public SoundHandler( SoundEvent event, SoundSource source ) {
		this( event, source, DEFAULT_VOLUME, DEFAULT_PITCH );
	}

	public void play( ServerLevel level, Vec3 position ) {
		this.play( level, position, this.volumeProvider, this.pitchProvider );
	}

	public void play( ServerLevel level, Vec3 position, Supplier< Float > volumeProvider ) {
		this.play( level, position, volumeProvider, this.pitchProvider );
	}

	public void play( ServerLevel level, Vec3 position, Supplier< Float > volumeProvider, Supplier< Float > pitchProvider ) {
		level.playSound( null, position.x, position.y, position.z, this.event, this.source, volumeProvider.get(), pitchProvider.get() );
	}
}
