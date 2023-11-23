package com.majruszlibrary.registry;

import com.majruszlibrary.platform.Services;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;

import java.nio.file.Path;

public class Registries {
	final static IRegistryPlatform PLATFORM = Services.load( IRegistryPlatform.class );
	public final static IRegistryPlatform.IAccessor< Item > ITEMS = PLATFORM.getItems();
	public final static IRegistryPlatform.IAccessor< MobEffect > EFFECTS = PLATFORM.getEffects();
	public final static IRegistryPlatform.IAccessor< Enchantment > ENCHANTMENTS = PLATFORM.getEnchantments();
	public final static IRegistryPlatform.IAccessor< EntityType< ? > > ENTITY_TYPES = PLATFORM.getEntityTypes();
	public final static IRegistryPlatform.IAccessor< SoundEvent > SOUND_EVENTS = PLATFORM.getSoundEvents();

	public static Path getConfigPath() {
		return PLATFORM.getConfigPath();
	}
}
