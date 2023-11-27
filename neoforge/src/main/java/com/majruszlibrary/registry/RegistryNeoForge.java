package com.majruszlibrary.registry;

import com.majruszlibrary.mixin.IMixinCriteriaTriggers;
import com.majruszlibrary.modhelper.DataNeoForge;
import com.majruszlibrary.platform.Side;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.Iterator;
import java.util.function.Function;

public class RegistryNeoForge implements IRegistryPlatform {
	@Override
	public < Type > void register( RegistryGroup< Type > group ) {
		DataNeoForge data = group.helper.getData( DataNeoForge.class );
		data.lastDeferredRegister = DeferredRegister.create( group.registry.key(), group.helper.getModId() );
		data.lastDeferredRegister.register( FMLJavaModLoadingContext.get().getModEventBus() );
	}

	@Override
	public < Type > void register( RegistryObject< Type > object ) {
		DataNeoForge data = object.group.helper.getData( DataNeoForge.class );
		net.minecraftforge.registries.RegistryObject< Type > forgeObject = ( ( DeferredRegister< Type > )data.lastDeferredRegister ).register( object.id, object.newInstance );
		object.set( forgeObject, forgeObject::isPresent );
	}

	@Override
	public void register( RegistryCallbacks callbacks ) {
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
		eventBus.addListener( ( FMLCommonSetupEvent event )->{
			callbacks.execute( Custom.Advancements.class, IMixinCriteriaTriggers::register );
		} );
		eventBus.addListener( ( EntityAttributeCreationEvent event )->{
			callbacks.execute( Custom.Attributes.class, event::put );
		} );
		eventBus.addListener( ( final SpawnPlacementRegisterEvent event )->{
			callbacks.execute( Custom.SpawnPlacements.class, new Custom.SpawnPlacements() {
				@Override
				public < Type extends Mob > void register( EntityType< Type > entityType, SpawnPlacements.Type type, Heightmap.Types heightmap,
					SpawnPlacements.SpawnPredicate< Type > predicate
				) {
					event.register( entityType, type, heightmap, predicate, SpawnPlacementRegisterEvent.Operation.AND );
				}
			} );
		} );

		Side.runOnClient( ()->()->{
			eventBus.addListener( ( final FMLClientSetupEvent event )->{
				callbacks.execute( Custom.ItemProperties.class, ItemProperties::register );
				callbacks.execute( Custom.ModelLayers.class, ForgeHooksClient::registerLayerDefinition );
				callbacks.execute( Custom.Renderers.class, EntityRenderers::register );
			} );
			eventBus.addListener( ( final RegisterParticleProvidersEvent event )->{
				callbacks.execute( Custom.Particles.class, new Custom.Particles() {
					@Override
					public < Type extends ParticleOptions > void register( ParticleType< Type > type,
						Function< SpriteSet, ParticleProvider< Type > > factory
					) {
						event.registerSpriteSet( type, factory::apply );
					}
				} );
			} );
		} );
	}

	@Override
	public IAccessor< Item > getItems() {
		return new Accessor<>( ForgeRegistries.ITEMS );
	}

	@Override
	public IAccessor< MobEffect > getEffects() {
		return new Accessor<>( ForgeRegistries.MOB_EFFECTS );
	}

	@Override
	public IAccessor< Enchantment > getEnchantments() {
		return new Accessor<>( ForgeRegistries.ENCHANTMENTS );
	}

	@Override
	public IAccessor< EntityType< ? > > getEntityTypes() {
		return new Accessor<>( ForgeRegistries.ENTITY_TYPES );
	}

	@Override
	public IAccessor< SoundEvent > getSoundEvents() {
		return new Accessor<>( ForgeRegistries.SOUND_EVENTS );
	}

	@Override
	public Path getConfigPath() {
		return FMLPaths.CONFIGDIR.get();
	}

	private static class Accessor< Type > implements IAccessor< Type > {
		private final IForgeRegistry< Type > registry;

		public Accessor( IForgeRegistry< Type > registry ) {
			this.registry = registry;
		}

		@Override
		public ResourceLocation getId( Type value ) {
			return this.registry.getKey( value );
		}

		@Override
		public Type get( ResourceLocation id ) {
			return this.registry.getValue( id );
		}

		@Override
		public Iterable< Type > get() {
			return this.registry;
		}

		@Override
		public Holder< Type > getHolder( Type value ) {
			return this.registry.getHolder( value ).orElseThrow();
		}

		@Override
		public @NotNull Iterator< Type > iterator() {
			return this.registry.iterator();
		}
	}
}
