package com.mlib.registry;

import com.mlib.platform.Services;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;

public class Registries {
	final static IRegistryPlatform PLATFORM = Services.load( IRegistryPlatform.class );

	public static ResourceLocation get( Item item ) {
		return PLATFORM.get( item );
	}

	public static ResourceLocation get( EntityType< ? > entityType ) {
		return PLATFORM.get( entityType );
	}

	public static ResourceLocation get( Enchantment enchantment ) {
		return PLATFORM.get( enchantment );
	}

	public static ResourceLocation get( Level level ) {
		return PLATFORM.get( level );
	}

	public static Item getItem( ResourceLocation id ) {
		return PLATFORM.getItem( id );
	}

	public static EntityType< ? > getEntityType( ResourceLocation id ) {
		return PLATFORM.getEntityType( id );
	}

	public static Enchantment getEnchantment( ResourceLocation id ) {
		return PLATFORM.getEnchantment( id );
	}
}
