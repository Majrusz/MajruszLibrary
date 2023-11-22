package com.mlib.registry;

import com.mlib.annotation.Dist;
import com.mlib.annotation.OnlyIn;
import com.mlib.contexts.OnParticlesRegisteredFabric;
import com.mlib.platform.Side;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;

import java.nio.file.Path;
import java.util.function.Function;

public class RegistryFabric implements IRegistryPlatform {
	@Override
	public < Type > void register( RegistryGroup< Type > group ) {}

	@Override
	public < Type > void register( RegistryObject< Type > object ) {
		Type value = object.newInstance.get();

		Registry.register( ( Registry< ? super Type > )object.group.registry, object.group.helper.getLocation( object.id ), value );
		object.set( ()->value, ()->true );
	}

	@Override
	public void register( RegistryCallbacks callbacks ) {
		Side.runOnClient( ()->()->{
			callbacks.execute( Custom.Particles.class, this::registerParticles );
		} );
	}

	@Override
	public IAccessor< Item > getItems() {
		return new IAccessor<>() {
			@Override
			public ResourceLocation get( Item value ) {
				return BuiltInRegistries.ITEM.getKey( value );
			}

			@Override
			public Item get( ResourceLocation id ) {
				return BuiltInRegistries.ITEM.get( id );
			}

			@Override
			public Iterable< Item > get() {
				return BuiltInRegistries.ITEM;
			}
		};
	}

	@Override
	public IAccessor< MobEffect > getEffects() {
		return new IAccessor<>() {
			@Override
			public ResourceLocation get( MobEffect value ) {
				return BuiltInRegistries.MOB_EFFECT.getKey( value );
			}

			@Override
			public MobEffect get( ResourceLocation id ) {
				return BuiltInRegistries.MOB_EFFECT.get( id );
			}

			@Override
			public Iterable< MobEffect > get() {
				return BuiltInRegistries.MOB_EFFECT;
			}
		};
	}

	@Override
	public IAccessor< Enchantment > getEnchantments() {
		return new IAccessor<>() {
			@Override
			public ResourceLocation get( Enchantment value ) {
				return BuiltInRegistries.ENCHANTMENT.getKey( value );
			}

			@Override
			public Enchantment get( ResourceLocation id ) {
				return BuiltInRegistries.ENCHANTMENT.get( id );
			}

			@Override
			public Iterable< Enchantment > get() {
				return BuiltInRegistries.ENCHANTMENT;
			}
		};
	}

	@Override
	public IAccessor< EntityType< ? > > getEntityTypes() {
		return new IAccessor<>() {
			@Override
			public ResourceLocation get( EntityType< ? > value ) {
				return BuiltInRegistries.ENTITY_TYPE.getKey( value );
			}

			@Override
			public EntityType< ? > get( ResourceLocation id ) {
				return BuiltInRegistries.ENTITY_TYPE.get( id );
			}

			@Override
			public Iterable< EntityType< ? > > get() {
				return BuiltInRegistries.ENTITY_TYPE;
			}
		};
	}

	@Override
	public Path getConfigPath() {
		return FabricLoader.getInstance().getConfigDir();
	}

	@OnlyIn( Dist.CLIENT )
	private < Type extends ParticleOptions > void registerParticles( ParticleType< Type > type, Function< SpriteSet, ParticleProvider< Type > > factory ) {
		OnParticlesRegisteredFabric.listen( data->data.engine.register( type, factory::apply ) );
	}
}
