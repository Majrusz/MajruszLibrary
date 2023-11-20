package com.majruszlibrary.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;

import java.nio.file.Path;

public interface IRegistryPlatform {
	< Type > void register( RegistryGroup< Type > group );

	< Type > void register( RegistryObject< Type > object );

	void register( RegistryCallbacks callbacks );

	IAccessor< Item > getItems();

	IAccessor< MobEffect > getEffects();

	IAccessor< Enchantment > getEnchantments();

	IAccessor< EntityType< ? > > getEntityTypes();

	IAccessor< SoundEvent > getSoundEvents();

	Path getConfigPath();

	interface IAccessor< Type > {
		ResourceLocation get( Type value );

		Type get( ResourceLocation id );

		Iterable< Type > get();
	}
}
