package com.majruszlibrary.registry;

import com.majruszlibrary.annotation.Dist;
import com.majruszlibrary.annotation.OnlyIn;
import com.majruszlibrary.events.OnParticlesRegisteredFabric;
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
import net.minecraft.core.Holder;
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
		return new Accessor<>( BuiltInRegistries.ITEM );
	}

	@Override
	public IAccessor< MobEffect > getEffects() {
		return new Accessor<>( BuiltInRegistries.MOB_EFFECT );
	}

	@Override
	public IAccessor< Enchantment > getEnchantments() {
		return new Accessor<>( BuiltInRegistries.ENCHANTMENT );
	}

	@Override
	public IAccessor< EntityType< ? > > getEntityTypes() {
		return new Accessor<>( BuiltInRegistries.ENTITY_TYPE );
	}

	@Override
	public IAccessor< SoundEvent > getSoundEvents() {
		return new Accessor<>( BuiltInRegistries.SOUND_EVENT );
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

	private < Type extends Mob > void registerSpawnPlacement( EntityType< Type > entityType, SpawnPlacements.Type type, Heightmap.Types heightmap,
		SpawnPlacements.SpawnPredicate< Type > predicate
	) {
		IMixinSpawnPlacements.register( entityType, type, heightmap, predicate );
	}

	private static class Accessor< Type > implements IAccessor< Type > {
		private final Registry< Type > registry;

		public Accessor( Registry< Type > registry ) {
			this.registry = registry;
		}

		@Override
		public ResourceLocation getId( Type value ) {
			return this.registry.getKey( value );
		}

		@Override
		public Type get( ResourceLocation id ) {
			return this.registry.get( id );
		}

		@Override
		public Iterable< Type > get() {
			return this.registry;
		}

		@Override
		public Holder< Type > getHolder( Type value ) {
			return this.registry.wrapAsHolder( value );
		}
	}
}
