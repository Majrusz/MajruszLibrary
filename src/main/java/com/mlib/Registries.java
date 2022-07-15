package com.mlib;

import com.mlib.loot_modifiers.AnyModification;
import com.mlib.loot_modifiers.HarvestCrop;
import com.mlib.loot_modifiers.SmeltingItems;
import com.mlib.registries.DeferredRegisterHelper;
import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class Registries {
	static final DeferredRegisterHelper HELPER = new DeferredRegisterHelper( MajruszLibrary.MOD_ID );
	static final DeferredRegister< Codec< ? extends IGlobalLootModifier > > LOOT_MODIFIERS = HELPER.create( ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS );

	static {
		LOOT_MODIFIERS.register( "any_situation", AnyModification.CODEC );
		LOOT_MODIFIERS.register( "harvest_crop", HarvestCrop.CODEC );
		LOOT_MODIFIERS.register( "smelting_items", SmeltingItems.CODEC );
	}

	public static void initialize() {
		HELPER.registerAll();
	}

	public static ResourceLocation getLocation( String register ) {
		return HELPER.getLocation( register );
	}
}
