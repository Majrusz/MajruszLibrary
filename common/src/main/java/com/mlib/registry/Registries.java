package com.mlib.registry;

import com.mlib.platform.Services;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;

import java.nio.file.Path;

public class Registries {
	final static IRegistryPlatform PLATFORM = Services.load( IRegistryPlatform.class );
	final static IRegistryPlatform.IAccessor< Item > ITEMS = PLATFORM.getItems();
	final static IRegistryPlatform.IAccessor< Enchantment > ENCHANTMENTS = PLATFORM.getEnchantments();
	final static IRegistryPlatform.IAccessor< EntityType< ? > > ENTITY_TYPES = PLATFORM.getEntityTypes();

	public static ResourceLocation get( Item item ) {
		return ITEMS.get( item );
	}

	public static ResourceLocation get( Enchantment enchantment ) {
		return ENCHANTMENTS.get( enchantment );
	}

	public static ResourceLocation get( EntityType< ? > entityType ) {
		return ENTITY_TYPES.get( entityType );
	}

	public static Item getItem( ResourceLocation id ) {
		return ITEMS.get( id );
	}

	public static Enchantment getEnchantment( ResourceLocation id ) {
		return ENCHANTMENTS.get( id );
	}

	public static EntityType< ? > getEntityType( ResourceLocation id ) {
		return ENTITY_TYPES.get( id );
	}

	public static Iterable< Item > getItems() {
		return ITEMS.get();
	}

	public static Iterable< Enchantment > getEnchantments() {
		return ENCHANTMENTS.get();
	}

	public static Iterable< EntityType< ? > > getEntityTypes() {
		return ENTITY_TYPES.get();
	}

	public static Path getConfigPath() {
		return PLATFORM.getConfigPath();
	}
}
