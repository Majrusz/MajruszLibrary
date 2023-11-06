package com.mlib.particles;

import com.mlib.data.Serializable;
import com.mlib.data.Serializables;
import com.mlib.registry.RegistryObject;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;

public class CustomParticleOptions< Type extends CustomParticleOptions< ? > > implements ParticleOptions {
	final Serializable< Type > serializable;
	final RegistryObject< ? extends ParticleType< ? > > object;

	public CustomParticleOptions( Class< Type > clazz, RegistryObject< ? extends ParticleType< ? > > object ) {
		this.serializable = Serializables.get( clazz );
		this.object = object;
	}

	@Override
	public ParticleType< ? > getType() {
		return this.object.get();
	}

	@Override
	public void writeToNetwork( FriendlyByteBuf buffer ) {
		this.serializable.write( ( Type )this, buffer );
	}

	@Override
	public String writeToString() {
		return this.object.getId();
	}
}
