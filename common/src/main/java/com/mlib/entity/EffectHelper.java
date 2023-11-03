package com.mlib.entity;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

import java.util.Optional;
import java.util.function.Supplier;

public class EffectHelper {
	public static Applier createApplier( Supplier< ? extends MobEffect > effect ) {
		return new Applier( effect );
	}

	public static boolean has( Supplier< ? extends MobEffect > effect, LivingEntity entity ) {
		return entity.hasEffect( effect.get() );
	}

	public static Optional< Integer > getAmplifier( Supplier< ? extends MobEffect > effect, LivingEntity entity ) {
		return Optional.ofNullable( entity.getEffect( effect.get() ) ).map( MobEffectInstance::getAmplifier );
	}

	public static Optional< Integer > getDuration( Supplier< ? extends MobEffect > effect, LivingEntity entity ) {
		return Optional.ofNullable( entity.getEffect( effect.get() ) ).map( MobEffectInstance::getDuration );
	}

	public static class Applier {
		final Supplier< ? extends MobEffect > effect;
		Integer maxDuration = null;
		Integer maxAmplifier = null;
		int duration = 100;
		int amplifier = 0;

		public Applier duration( int duration ) {
			this.duration = duration;

			return this;
		}

		public Applier amplifier( int amplifier ) {
			this.amplifier = amplifier;

			return this;
		}

		public Applier stackableDuration( int max ) {
			this.maxDuration = max;

			return this;
		}

		public Applier stackableAmplifier( int max ) {
			this.maxAmplifier = max;

			return this;
		}

		public void apply( LivingEntity entity ) {
			int duration = this.duration;
			int amplifier = this.amplifier;
			MobEffectInstance previous = entity.getEffect( this.effect.get() );
			if( previous != null ) {
				if( this.maxDuration != null ) {
					duration = Math.min( duration + previous.getDuration(), this.maxDuration );
				}
				if( this.maxAmplifier != null ) {
					amplifier = Math.min( amplifier + previous.getAmplifier() + 1, this.maxAmplifier );
				}
			}

			entity.addEffect( new MobEffectInstance( this.effect.get(), duration, amplifier ) );
		}

		private Applier( Supplier< ? extends MobEffect > effect ) {
			this.effect = effect;
		}
	}
}
