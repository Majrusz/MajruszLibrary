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
	final Supplier< ? extends MobEffect > effect;
	final IntegerConfig amplifier;
	final DoubleConfig duration;

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

	public void apply( LivingEntity entity, int extraAmplifier, int extraDuration ) {
		MobEffectHelper.tryToApply( entity, this.getEffect(), this.getDuration() + extraDuration, this.getAmplifier() + extraAmplifier );
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
}
