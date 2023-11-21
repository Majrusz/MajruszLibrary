package com.majruszlibrary.mixin;

import com.majruszlibrary.events.OnParticlesRegistered;
import com.majruszlibrary.events.base.Events;
import net.minecraft.client.particle.ParticleEngine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin( ParticleEngine.class )
public abstract class MixinParticleEngine {
	@Inject(
		at = @At( "TAIL" ),
		method = "registerProviders ()V"
	)
	private void registerProviders( CallbackInfo callback ) {
		Events.dispatch( new OnParticlesRegistered( ( ParticleEngine )( Object )this ) );
	}
}
