package com.mlib.gamemodifiers.configs;

import com.mlib.config.DoubleConfig;
import com.mlib.math.Range;
import com.mlib.mobeffects.MobEffectHelper;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class StackableEffectConfig extends EffectConfig {
	static final Range< Double > MAX_DURATION = new Range<>( 5.0, 9999.0 );
	final DoubleConfig maxDuration;

	public StackableEffectConfig( Supplier< MobEffect > effect, int amplifier, double duration, double maxDuration ) {
		super( effect, amplifier, duration );

		this.maxDuration = new DoubleConfig( maxDuration, MAX_DURATION );
		this.addConfig( this.maxDuration.name( "maximum_duration" ).comment( "Maximum duration in seconds it can reach." ) );
	}

	public StackableEffectConfig( RegistryObject< ? extends MobEffect > effect, int amplifier, double duration,
		double maxDuration
	) {
		this( effect::get, amplifier, duration, maxDuration );
	}

	public StackableEffectConfig( MobEffect effect, int amplifier, double duration, double maxDuration ) {
		this( ()->effect, amplifier, duration, maxDuration );
	}

	@Override
	public void apply( LivingEntity entity, int extraAmplifier, int extraDuration ) {
		MobEffectHelper.tryToStack( entity, this.getEffect(), this.getDuration() + extraDuration, this.getAmplifier() + extraAmplifier, this.getMaxDuration() );
	}

	public int getMaxDuration() {
		return this.maxDuration.asTicks();
	}
}
