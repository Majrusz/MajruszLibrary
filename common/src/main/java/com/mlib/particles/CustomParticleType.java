package com.mlib.particles;

import com.mlib.data.Serializable;
import com.mlib.data.Serializables;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.Supplier;

public class CustomParticleType< Type extends ParticleOptions > extends ParticleType< Type > {
	private final Serializable< Type > serializable;
	private final Codec< Type > codec;

	public CustomParticleType( Class< Type > clazz, Supplier< Type > instance ) {
		super( true, new ParticleOptions.Deserializer<>() {
			final Serializable< Type > serializable = Serializables.get( clazz );

			@Override
			public Type fromCommand( ParticleType< Type > type, StringReader string ) throws CommandSyntaxException {
				return instance.get();
			}

			@Override
			public Type fromNetwork( ParticleType< Type > type, FriendlyByteBuf buffer ) {
				Type object = instance.get();
				this.serializable.read( object, buffer );

				return object;
			}
		} );

		this.serializable = Serializables.get( clazz );
		this.codec = new Codec<>() {
			@Override
			public < T > DataResult< Pair< Type, T > > decode( DynamicOps< T > ops, T input ) {
				return DataResult.error( ()->"" );
			}

			@Override
			public < T > DataResult< T > encode( Type input, DynamicOps< T > ops, T prefix ) {
				return DataResult.error( ()->"" );
			}
		};
	}

	@Override
	public Codec< Type > codec() {
		return this.codec; // TODO: add support for codecs in serializable structures
	}
}
