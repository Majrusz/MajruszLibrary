package com.mlib.particles;

import com.mlib.data.Serializables;
import com.mlib.registry.RegistryObject;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;

public class CustomParticleOptions< Type extends CustomParticleOptions< ? > > implements ParticleOptions {
	final RegistryObject< ? extends ParticleType< ? > > object;

	public CustomParticleOptions( RegistryObject< ? extends ParticleType< ? > > object ) {
		this.object = object;
	}

	@Override
	public ParticleType< ? > getType() {
		return this.object.get();
	}

	@Override
	public void writeToNetwork( FriendlyByteBuf buffer ) {
		Serializables.get( this ).writeBuffer( this, buffer );
	}

	@Override
	public String writeToString() {
		return this.object.getId();
	}
}
