package com.mlib.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;

import java.nio.file.Path;

public interface IRegistryPlatform {
	< Type > void register( RegistryGroup< Type > group );

	< Type > void register( RegistryObject< Type > object );

	IAccessor< Item > getItems();

	IAccessor< Enchantment > getEnchantments();

	IAccessor< EntityType< ? > > getEntityTypes();

	Path getConfigPath();

	interface IAccessor< Type > {
		ResourceLocation get( Type value );

		Type get( ResourceLocation id );

		Iterable< Type > get();
	}
}
