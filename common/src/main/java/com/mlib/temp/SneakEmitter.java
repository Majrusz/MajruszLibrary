package com.mlib.temp;

import com.mlib.MajruszLibrary;
import com.mlib.annotation.AutoInstance;
import com.mlib.contexts.OnPlayerTicked;
import com.mlib.contexts.base.Condition;
import com.mlib.emitter.ParticleEmitter;
import com.mlib.math.AnyPos;
import com.mlib.time.TimeHelper;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.phys.Vec3;

@AutoInstance
public class SneakEmitter {
	public SneakEmitter() {
		OnPlayerTicked.listen( this::emitParticle )
			.addCondition( Condition.isLogicalServer() )
			.addCondition( Condition.predicate( data->data.player.isCrouching() ) );

		OnPlayerTicked.listen( this::emitParticle2 )
			.addCondition( Condition.isLogicalServer() )
			.addCondition( Condition.predicate( data->TimeHelper.haveTicksPassed( 40 ) ) )
			.addCondition( Condition.predicate( data->data.player.isCrouching() ) );
	}

	private void emitParticle( OnPlayerTicked data ) {
		ParticleEmitter.of( ( SimpleParticleType )MajruszLibrary.THE_PARTICLE.get() )
			.count( 2 )
			.emit( data.getServerLevel(), data.player.position() );
	}

	private void emitParticle2( OnPlayerTicked data ) {
		AnyPos pos = AnyPos.from( data.player.position() );
		TimeHelper.slider( 2.0, slider->{
			float z = 0.5f * slider.getRatio() * slider.getRatio();
			ParticleEmitter.of( ( SimpleParticleType )MajruszLibrary.THE_PARTICLE.get() )
				.offset( ()->new Vec3( 0.1f + z, 0.0f, 0.1f + z ) )
				.count( ( int )( slider.getRatio() * 100 ) )
				.emit( data.getServerLevel(), pos.add( 0.0, slider.getRatio() * 5.0f, 0.0 ).vec3() );
		} );
	}
}
