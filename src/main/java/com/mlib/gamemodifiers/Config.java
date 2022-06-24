package com.mlib.gamemodifiers;

import com.mlib.config.ConfigGroup;
import com.mlib.config.DoubleConfig;
import com.mlib.config.IntegerConfig;
import com.mlib.effects.EffectHelper;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;

import java.util.Optional;
import java.util.function.Supplier;

public abstract class Config {
	final String groupName;
	final String groupComment;

	public Config( String groupName, String groupComment ) {
		this.groupName = groupName;
		this.groupComment = groupComment;
	}

	public abstract void setup( ConfigGroup group );

	public ConfigGroup addNewGroup( ConfigGroup group ) {
		return group.addNewGroup( this.groupName, this.groupComment );
	}

	public static class Effect extends Config {
		static final int MIN_AMPLIFIER = 0, MAX_AMPLIFIER = 9;
		static final double MIN_DURATION = 1.0, MAX_DURATION = 99.0;
		static final double MIN_LIMIT = 5.0, MAX_LIMIT = 999.0;
		final Supplier< MobEffect > effect;
		final IntegerConfig amplifier;
		final DoubleConfig duration;
		final Optional< DoubleConfig > maxDuration;

		public Effect( String groupName, Supplier< MobEffect > effect, int amplifier, double duration, Optional< Double > maxDuration ) {
			super( groupName, "" );
			this.effect = effect;
			this.amplifier = new IntegerConfig( "amplifier", "Level of the effect to apply.", false, amplifier, MIN_AMPLIFIER, MAX_AMPLIFIER );
			this.duration = new DoubleConfig( "duration", "Duration in seconds.", false, duration, MIN_DURATION, MAX_DURATION );
			this.maxDuration = maxDuration.map( value->new DoubleConfig( "maximum_duration", "Maximum duration in seconds it can reach.", false, value, MIN_LIMIT, MAX_LIMIT ) );
		}

		public Effect( String groupName, Supplier< MobEffect > effect, int amplifier, double duration, double maxDuration ) {
			this( groupName, effect, amplifier, duration, Optional.of( maxDuration ) );
		}

		public Effect( String groupName, Supplier< MobEffect > effect, int amplifier, double duration ) {
			this( groupName, effect, amplifier, duration, Optional.empty() );
		}

		@Override
		public void setup( ConfigGroup group ) {
			group.addConfigs( this.amplifier, this.duration );
			this.maxDuration.ifPresent( group::addConfig );
		}

		public void apply( LivingEntity entity ) {
			if( this.maxDuration.isPresent() ) {
				EffectHelper.stackEffectIfPossible( entity, getEffect(), getDuration(), getAmplifier(), getMaxDuration() );
			} else {
				EffectHelper.applyEffectIfPossible( entity, getEffect(), getDuration(), getAmplifier() );
			}
		}

		public MobEffect getEffect() {
			return this.effect.get();
		}

		public int getAmplifier() {
			return this.amplifier.get();
		}

		public int getDuration() {
			return this.duration.asTicks();
		}

		public int getMaxDuration() {
			assert this.maxDuration.isPresent();

			return this.maxDuration.get().asTicks();
		}
	}
}
