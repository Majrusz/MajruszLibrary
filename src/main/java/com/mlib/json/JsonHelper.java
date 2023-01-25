package com.mlib.json;

import com.google.gson.JsonObject;
import com.mlib.Utility;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;

public class JsonHelper {
	public static int getAsInt( JsonObject object, String key ) {
		return GsonHelper.getAsInt( object, key );
	}

	public static int getAsInt( JsonObject object, String key, int defaultValue ) {
		return GsonHelper.getAsInt( object, key, defaultValue );
	}

	public static float getAsFloat( JsonObject object, String key ) {
		return GsonHelper.getAsFloat( object, key );
	}

	public static float getAsFloat( JsonObject object, String key, float defaultValue ) {
		return GsonHelper.getAsFloat( object, key, defaultValue );
	}

	public static String getAsString( JsonObject object, String key ) {
		return GsonHelper.getAsString( object, key );
	}

	public static String getAsString( JsonObject object, String key, String defaultValue ) {
		return GsonHelper.getAsString( object, key, defaultValue );
	}

	public static ResourceLocation getAsLocation( JsonObject object, String key ) {
		return new ResourceLocation( GsonHelper.getAsString( object, key ) );
	}

	public static ResourceLocation getAsLocation( JsonObject object, String key, ResourceLocation defaultValue ) {
		return object.has( key ) ? getAsLocation( object, key ) : defaultValue;
	}

	public static Item getAsItem( JsonObject object, String key ) {
		return Utility.getItem( getAsString( object, key ) );
	}

	public static Item getAsItem( JsonObject object, String key, Item defaultValue ) {
		return object.has( key ) ? getAsItem( object, key ) : defaultValue;
	}

	public static EntityType< ? > getAsEntity( JsonObject object, String key ) {
		return Utility.getEntity( getAsString( object, key ) );
	}

	public static EntityType< ? > getAsEntity( JsonObject object, String key, EntityType< ? > defaultValue ) {
		return object.has( key ) ? getAsEntity( object, key ) : defaultValue;
	}

	public static Enchantment getAsEnchantment( JsonObject object, String key ) {
		return Utility.getEnchantment( getAsString( object, key ) );
	}

	public static Enchantment getAsEnchantment( JsonObject object, String key, Enchantment defaultValue ) {
		return object.has( key ) ? getAsEnchantment( object, key ) : defaultValue;
	}
}
