package com.mlib.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
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
		object.set( forgeObject );
	}

	@Override
	public ResourceLocation get( Item item ) {
		return ForgeRegistries.ITEMS.getKey( item );
	}

	@Override
	public ResourceLocation get( EntityType< ? > entityType ) {
		return ForgeRegistries.ENTITY_TYPES.getKey( entityType );
	}

	@Override
	public ResourceLocation get( Enchantment enchantment ) {
		return ForgeRegistries.ENCHANTMENTS.getKey( enchantment );
	}

	@Override
	public ResourceLocation get( Level level ) {
		return level.dimension().location();
	}

	@Override
	public Item getItem( ResourceLocation id ) {
		return ForgeRegistries.ITEMS.getValue( id );
	}

	@Override
	public EntityType< ? > getEntityType( ResourceLocation id ) {
		return ForgeRegistries.ENTITY_TYPES.getValue( id );
	}

	@Override
	public Enchantment getEnchantment( ResourceLocation id ) {
		return ForgeRegistries.ENCHANTMENTS.getValue( id );
	}

	@Override
	public Path getConfigPath() {
		return FMLPaths.CONFIGDIR.get();
	}
}
