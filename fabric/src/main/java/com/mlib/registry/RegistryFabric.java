package com.mlib.registry;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;

import java.nio.file.Path;

public class RegistryFabric implements IRegistryPlatform {
	@Override
	public < Type > void register( RegistryGroup< Type > group ) {}

	@Override
	public < Type > void register( RegistryObject< Type > object ) {
		Type value = object.newInstance.get();

		Registry.register( ( Registry< ? super Type > )object.group.registry, object.group.helper.getLocation( object.id ), value );
		object.set( ()->value );
	}

	@Override
	public ResourceLocation get( Item item ) {
		return BuiltInRegistries.ITEM.getKey( item );
	}

	@Override
	public ResourceLocation get( EntityType< ? > entityType ) {
		return BuiltInRegistries.ENTITY_TYPE.getKey( entityType );
	}

	@Override
	public ResourceLocation get( Enchantment enchantment ) {
		return BuiltInRegistries.ENCHANTMENT.getKey( enchantment );
	}

	@Override
	public ResourceLocation get( Level level ) {
		return level.dimension().location();
	}

	@Override
	public Item getItem( ResourceLocation id ) {
		return BuiltInRegistries.ITEM.get( id );
	}

	@Override
	public EntityType< ? > getEntityType( ResourceLocation id ) {
		return BuiltInRegistries.ENTITY_TYPE.get( id );
	}

	@Override
	public Enchantment getEnchantment( ResourceLocation id ) {
		return BuiltInRegistries.ENCHANTMENT.get( id );
	}

	@Override
	public Path getConfigPath() {
		return FabricLoader.getInstance().getConfigDir();
	}
}
