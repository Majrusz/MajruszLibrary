package com.majruszlibrary.entity;

import com.majruszlibrary.data.Reader;
import com.majruszlibrary.data.Serializables;
import com.majruszlibrary.math.Range;
import com.majruszlibrary.text.TextHelper;
import com.majruszlibrary.time.TimeHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

import java.util.function.Supplier;

public class EffectDef {
	public Supplier< ? extends MobEffect > effect;
	public int amplifier;
	public float duration;

	static {
		Serializables.get( EffectDef.class )
			.define( "effect", Reader.mobEffect(), s->s.effect.get(), ( s, v )->s.effect = ()->v )
			.define( "amplifier", Reader.integer(), s->s.amplifier, ( s, v )->s.amplifier = Range.of( 0, 10 ).clamp( v ) )
			.define( "duration", Reader.number(), s->s.duration, ( s, v )->s.duration = Range.of( 1.0f, 1000.0f ).clamp( v ) );
	}

	public EffectDef( Supplier< ? extends MobEffect > effect, int amplifier, float duration ) {
		this.effect = effect;
		this.amplifier = amplifier;
		this.duration = duration;
	}

	public EffectDef() {
		this( ()->null, 0, 1.0f );
	}

	public MobEffectInstance toEffectInstance() {
		return new MobEffectInstance( this.effect.get(), TimeHelper.toTicks( this.duration ), this.amplifier );
	}

	public MutableComponent toComponent() {
		Component effectName = this.effect.get().getDisplayName();
		Component fullName = this.amplifier > 0 ? TextHelper.translatable( "potion.withAmplifier", effectName.getString(), TextHelper.toRoman( this.amplifier + 1 ) ) : TextHelper.literal( effectName.getString() );

		return TextHelper.translatable( "potion.withDuration", fullName.getString(), TextHelper.toEffectDuration( this.duration ) );
	}
}
