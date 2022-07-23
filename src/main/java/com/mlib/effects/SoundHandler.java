package com.mlib.effects;

import com.mlib.Random;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;

import java.util.function.Supplier;

public class SoundHandler {
	public static final SoundHandler AWARD = new SoundHandler( SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.AMBIENT, ()->0.05f );
	public static final SoundHandler SMELT = new SoundHandler( SoundEvents.FIRECHARGE_USE, SoundSource.AMBIENT, ()->0.05f );

	final SoundEvent event;
	final SoundSource source;
	final Supplier< Float > volumeProvider;
	final Supplier< Float > pitchProvider;

	public SoundHandler( SoundEvent event, SoundSource source, Supplier< Float > volumeProvider, Supplier< Float > pitchProvider ) {
		this.event = event;
		this.source = source;
		this.volumeProvider = volumeProvider;
		this.pitchProvider = pitchProvider;
	}

	public SoundHandler( SoundEvent event, SoundSource source, Supplier< Float > volumeProvider ) {
		this( event, source, volumeProvider, ()->Random.nextFloat( 0.8f, 1.2f ) );
	}

	public void play( ServerLevel level, Vec3 position ) {
		level.playSound( null, position.x, position.y, position.z, this.event, this.source, this.volumeProvider.get(), this.pitchProvider.get() );
	}
}
