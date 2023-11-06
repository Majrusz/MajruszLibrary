package com.mlib.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
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
	public Path getConfigPath() {
		return FMLPaths.CONFIGDIR.get();
	}
}
