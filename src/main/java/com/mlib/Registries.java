package com.mlib;

import com.mlib.loot_modifiers.AnyModification;
import com.mlib.loot_modifiers.HarvestCrop;
import com.mlib.loot_modifiers.SmeltingItems;
import com.mlib.registries.DeferredRegisterHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class Registries {
	static final DeferredRegisterHelper HELPER = new DeferredRegisterHelper( MajruszLibrary.MOD_ID );
	static final DeferredRegister< GlobalLootModifierSerializer< ? > > LOOT_MODIFIERS = HELPER.create( ForgeRegistries.Keys.LOOT_MODIFIER_SERIALIZERS );

	static {
		LOOT_MODIFIERS.register( "any_situation", AnyModification.Serializer::new );
		LOOT_MODIFIERS.register( "harvest_crop", HarvestCrop.Serializer::new );
		LOOT_MODIFIERS.register( "smelting_items", SmeltingItems.Serializer::new );
	}

	public static void initialize() {
		HELPER.registerAll();
	}

	public static ResourceLocation getLocation( String register ) {
		return HELPER.getLocation( register );
	}
}
