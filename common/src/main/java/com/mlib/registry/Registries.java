package com.mlib.registry;

import com.mlib.platform.Services;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;

import java.nio.file.Path;

public class Registries {
	final static IRegistryPlatform PLATFORM = Services.load( IRegistryPlatform.class );
	final static IRegistryPlatform.IAccessor< Item > ITEMS = PLATFORM.getItems();
	final static IRegistryPlatform.IAccessor< MobEffect > EFFECTS = PLATFORM.getEffects();
	final static IRegistryPlatform.IAccessor< Enchantment > ENCHANTMENTS = PLATFORM.getEnchantments();
	final static IRegistryPlatform.IAccessor< EntityType< ? > > ENTITY_TYPES = PLATFORM.getEntityTypes();
	final static IRegistryPlatform.IAccessor< SoundEvent > SOUND_EVENTS = PLATFORM.getSoundEvents();

	public static ResourceLocation get( Item item ) {
		return ITEMS.get( item );
	}

	public static ResourceLocation get( MobEffect effect ) {
		return EFFECTS.get( effect );
	}

	public static ResourceLocation get( Enchantment enchantment ) {
		return ENCHANTMENTS.get( enchantment );
	}

	public static ResourceLocation get( EntityType< ? > entityType ) {
		return ENTITY_TYPES.get( entityType );
	}

	public static ResourceLocation get( SoundEvent sound ) {
		return SOUND_EVENTS.get( sound );
	}

	public static Item getItem( ResourceLocation id ) {
		return ITEMS.get( id );
	}

	public static MobEffect getEffect( ResourceLocation id ) {
		return EFFECTS.get( id );
	}

	public static Enchantment getEnchantment( ResourceLocation id ) {
		return ENCHANTMENTS.get( id );
	}

	public static EntityType< ? > getEntityType( ResourceLocation id ) {
		return ENTITY_TYPES.get( id );
	}

	public static SoundEvent getSoundEvent( ResourceLocation id ) {
		return SOUND_EVENTS.get( id );
	}

	public static Iterable< Item > getItems() {
		return ITEMS.get();
	}

	public static Iterable< MobEffect > getEffects() {
		return EFFECTS.get();
	}

	public static Iterable< Enchantment > getEnchantments() {
		return ENCHANTMENTS.get();
	}

	public static Iterable< EntityType< ? > > getEntityTypes() {
		return ENTITY_TYPES.get();
	}

	public static Iterable< SoundEvent > getSoundEvents() {
		return SOUND_EVENTS.get();
	}

	public static Path getConfigPath() {
		return PLATFORM.getConfigPath();
	}
}
