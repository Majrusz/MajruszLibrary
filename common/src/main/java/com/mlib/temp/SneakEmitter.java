package com.mlib.temp;

import com.mlib.MajruszLibrary;
import com.mlib.annotation.AutoInstance;
import com.mlib.contexts.OnPlayerTicked;
import com.mlib.contexts.base.Condition;
import com.mlib.emitter.ParticleEmitter;
import net.minecraft.core.particles.SimpleParticleType;

@AutoInstance
public class SneakEmitter {
	public SneakEmitter() {
		OnPlayerTicked.listen( this::emitParticle )
			.addCondition( Condition.isLogicalServer() )
			.addCondition( Condition.predicate( data->data.player.isCrouching() ) );
	}

	private void emitParticle( OnPlayerTicked data ) {
		ParticleEmitter.of( ( SimpleParticleType )MajruszLibrary.THE_PARTICLE.get() )
			.count( 2 )
			.emit( data.getServerLevel(), data.player.position() );
	}
}
