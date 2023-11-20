package com.majruszlibrary.registry;

import com.majruszlibrary.mixin.IMixinCriteriaTriggers;
import com.majruszlibrary.platform.Side;
import net.minecraft.client.renderer.entity.EntityRenderers;
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
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.nio.file.Path;

public class RegistryNeoForge implements IRegistryPlatform {
	DeferredRegister< ? > lastDeferredRegister = null;

	@Override
	public < Type > void register( RegistryGroup< Type > group ) {
		this.lastDeferredRegister = DeferredRegister.create( group.registry.key(), group.helper.getModId() );
		this.lastDeferredRegister.register( FMLJavaModLoadingContext.get().getModEventBus() );
	}

	@Override
	public < Type > void register( RegistryObject< Type > object ) {
		net.minecraftforge.registries.RegistryObject< Type > forgeObject = ( ( DeferredRegister< Type > )this.lastDeferredRegister ).register( object.id, object.newInstance );
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
				callbacks.execute( Custom.ModelLayers.class, ForgeHooksClient::registerLayerDefinition );
				callbacks.execute( Custom.Renderers.class, EntityRenderers::register );
			} );
		} );
	}

	@Override
	public IAccessor< Item > getItems() {
		return new IAccessor<>() {
			@Override
			public ResourceLocation get( Item value ) {
				return ForgeRegistries.ITEMS.getKey( value );
			}

			@Override
			public Item get( ResourceLocation id ) {
				return ForgeRegistries.ITEMS.getValue( id );
			}

			@Override
			public Iterable< Item > get() {
				return ForgeRegistries.ITEMS;
			}
		};
	}

	@Override
	public IAccessor< MobEffect > getEffects() {
		return new IAccessor<>() {
			@Override
			public ResourceLocation get( MobEffect value ) {
				return ForgeRegistries.MOB_EFFECTS.getKey( value );
			}

			@Override
			public MobEffect get( ResourceLocation id ) {
				return ForgeRegistries.MOB_EFFECTS.getValue( id );
			}

			@Override
			public Iterable< MobEffect > get() {
				return ForgeRegistries.MOB_EFFECTS;
			}
		};
	}

	@Override
	public IAccessor< Enchantment > getEnchantments() {
		return new IAccessor<>() {
			@Override
			public ResourceLocation get( Enchantment value ) {
				return ForgeRegistries.ENCHANTMENTS.getKey( value );
			}

			@Override
			public Enchantment get( ResourceLocation id ) {
				return ForgeRegistries.ENCHANTMENTS.getValue( id );
			}

			@Override
			public Iterable< Enchantment > get() {
				return ForgeRegistries.ENCHANTMENTS;
			}
		};
	}

	@Override
	public IAccessor< EntityType< ? > > getEntityTypes() {
		return new IAccessor<>() {
			@Override
			public ResourceLocation get( EntityType< ? > value ) {
				return ForgeRegistries.ENTITY_TYPES.getKey( value );
			}

			@Override
			public EntityType< ? > get( ResourceLocation id ) {
				return ForgeRegistries.ENTITY_TYPES.getValue( id );
			}

			@Override
			public Iterable< EntityType< ? > > get() {
				return ForgeRegistries.ENTITY_TYPES;
			}
		};
	}

	@Override
	public IAccessor< SoundEvent > getSoundEvents() {
		return new IAccessor<>() {
			@Override
			public ResourceLocation get( SoundEvent value ) {
				return ForgeRegistries.SOUND_EVENTS.getKey( value );
			}

			@Override
			public SoundEvent get( ResourceLocation id ) {
				return ForgeRegistries.SOUND_EVENTS.getValue( id );
			}

			@Override
			public Iterable< SoundEvent > get() {
				return ForgeRegistries.SOUND_EVENTS;
			}
		};
	}

	@Override
	public Path getConfigPath() {
		return FMLPaths.CONFIGDIR.get();
	}
}
