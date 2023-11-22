package com.mlib.mixin.fabric;

import com.mlib.contexts.OnParticlesRegisteredFabric;
import com.mlib.contexts.base.Contexts;
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
		Contexts.dispatch( new OnParticlesRegisteredFabric( ( ParticleEngine )( Object )this ) );
	}
}