package com.mlib.gamemodifiers.configs;

import com.mlib.config.ConfigGroup;
import com.mlib.config.DoubleConfig;
import com.mlib.config.IntegerConfig;
import com.mlib.math.Range;
import com.mlib.mobeffects.MobEffectHelper;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class EffectConfig extends ConfigGroup {
	static final Range< Integer > AMPLIFIER = new Range<>( 1, 10 );
	static final Range< Double > DURATION = new Range<>( 1.0, 999.0 );
	static final Range< Double > MAX_DURATION = new Range<>( 5.0, 9999.0 );
	final Supplier< ? extends MobEffect > effect;
	final IntegerConfig amplifier;
	final DoubleConfig duration;
	DoubleConfig maxDuration = null;

	public EffectConfig( Supplier< ? extends MobEffect > effect, int amplifier, double duration ) {
		this.effect = effect;
		this.amplifier = new IntegerConfig( amplifier + 1, AMPLIFIER );
		this.duration = new DoubleConfig( duration, DURATION );

		this.addConfig( this.amplifier.name( "amplifier" ).comment( "Level of the effect to apply." ) )
			.addConfig( this.duration.name( "duration" ).comment( "Duration in seconds." ) );
	}

	public EffectConfig( RegistryObject< ? extends MobEffect > effect, int amplifier, double duration ) {
		this( effect::get, amplifier, duration );
	}

	public EffectConfig( MobEffect effect, int amplifier, double duration ) {
		this( ()->effect, amplifier, duration );
	}

	public EffectConfig stackable( double maxDuration ) {
		this.maxDuration = new DoubleConfig( maxDuration, MAX_DURATION );

		this.addConfig( this.maxDuration.name( "maximum_duration" ).comment( "Maximum duration in seconds it can reach." ) );

		return this;
	}

	public void apply( LivingEntity entity, int extraAmplifier, int extraDuration ) {
		if( this.isStackable() ) {
			MobEffectHelper.tryToStack( entity, this.getEffect(), this.getDuration() + extraDuration, this.getAmplifier() + extraAmplifier, this.getMaxDuration() );
		} else {
			MobEffectHelper.tryToApply( entity, this.getEffect(), this.getDuration() + extraDuration, this.getAmplifier() + extraAmplifier );
		}
	}

	public void apply( LivingEntity entity ) {
		this.apply( entity, 0, 0 );
	}

	public MobEffect getEffect() {
		return this.effect.get();
	}

	public int getAmplifier() {
		return this.amplifier.get() - 1;
	}

	public int getDuration() {
		return this.duration.asTicks();
	}

	public boolean isStackable() {
		return this.maxDuration != null;
	}

	public int getMaxDuration() {
		return this.isStackable() ? this.maxDuration.asTicks() : 0;
	}
}
