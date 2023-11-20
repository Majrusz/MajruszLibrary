package com.majruszlibrary.registry;

import com.majruszlibrary.annotation.Dist;
import com.majruszlibrary.annotation.OnlyIn;
import com.majruszlibrary.contexts.OnParticlesRegisteredFabric;
import com.majruszlibrary.mixin.IMixinCriteriaTriggers;
import com.majruszlibrary.mixin.IMixinSpawnPlacements;
import com.majruszlibrary.platform.Side;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.levelgen.Heightmap;

import java.nio.file.Path;
import java.util.function.Function;
import java.util.function.Supplier;

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
		callbacks.execute( Custom.Advancements.class, this::registerAdvancement );
		callbacks.execute( Custom.Attributes.class, this::registerAttributes );
		callbacks.execute( Custom.SpawnPlacements.class, this::registerSpawnPlacement );

		Side.runOnClient( ()->()->{
			callbacks.execute( Custom.ModelLayers.class, this::registerModelLayer );
			callbacks.execute( Custom.Particles.class, this::registerParticles );
			callbacks.execute( Custom.Renderers.class, this::registerRenderer );
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
	public IAccessor< SoundEvent > getSoundEvents() {
		return new IAccessor<>() {
			@Override
			public ResourceLocation get( SoundEvent value ) {
				return BuiltInRegistries.SOUND_EVENT.getKey( value );
			}

			@Override
			public SoundEvent get( ResourceLocation id ) {
				return BuiltInRegistries.SOUND_EVENT.get( id );
			}

			@Override
			public Iterable< SoundEvent > get() {
				return BuiltInRegistries.SOUND_EVENT;
			}
		};
	}

	@Override
	public Path getConfigPath() {
		return FabricLoader.getInstance().getConfigDir();
	}

	private < Type extends CriterionTrigger< ? > > void registerAdvancement( Type trigger ) {
		IMixinCriteriaTriggers.register( trigger );
	}

	private < Type extends LivingEntity > void registerAttributes( EntityType< Type > type, AttributeSupplier attributes ) {
		FabricDefaultAttributeRegistry.register( type, attributes );
	}

	private < Type extends Mob > void registerSpawnPlacement( EntityType< Type > entityType, SpawnPlacements.Type type, Heightmap.Types heightmap,
		SpawnPlacements.SpawnPredicate< Type > predicate
	) {
		IMixinSpawnPlacements.register( entityType, type, heightmap, predicate );
	}

	@OnlyIn( Dist.CLIENT )
	private void registerModelLayer( ModelLayerLocation id, Supplier< LayerDefinition > definition ) {
		EntityModelLayerRegistry.registerModelLayer( id, definition::get );
	}

	@OnlyIn( Dist.CLIENT )
	private < Type extends ParticleOptions > void registerParticles( ParticleType< Type > type, Function< SpriteSet, ParticleProvider< Type > > factory ) {
		OnParticlesRegisteredFabric.listen( data->data.engine.register( type, factory::apply ) );
	}

	@OnlyIn( Dist.CLIENT )
	private < Type extends Entity > void registerRenderer( EntityType< Type > type, EntityRendererProvider< Type > factory ) {
		EntityRendererRegistry.register( type, factory );
	}
}
