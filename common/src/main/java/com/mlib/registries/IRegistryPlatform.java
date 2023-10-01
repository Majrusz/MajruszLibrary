package com.mlib.registries;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;

public interface IRegistryPlatform {
	< Type > void register( RegistryGroup< Type > group );

	< Type > void register( RegistryObject< Type > object );

	ResourceLocation get( Item item );

	ResourceLocation get( EntityType< ? > entityType );

	ResourceLocation get( Enchantment enchantment );

	ResourceLocation get( Level level );

	Item getItem( ResourceLocation id );

	EntityType< ? > getEntityType( ResourceLocation id );

	Enchantment getEnchantment( ResourceLocation id );
}
