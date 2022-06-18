package com.mlib;

import com.mlib.loot_modifiers.*;
import com.mlib.registries.DeferredRegisterHelper;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RegistryHandler {
	private static final DeferredRegisterHelper REGISTER_HELPER = new DeferredRegisterHelper( MajruszLibrary.MOD_ID );

	public static void registerAll() {
		REGISTER_HELPER.create( ForgeRegistries.Keys.LOOT_MODIFIER_SERIALIZERS, RegistryHandler::registerLootModifiers );
		REGISTER_HELPER.registerAll();
	}

	private static void registerLootModifiers( DeferredRegister< GlobalLootModifierSerializer< ? > > lootModifiers ) {
		lootModifiers.register( "any_situation", AnyModification.Serializer::new );
		lootModifiers.register( "harvest_crop", HarvestCrop.Serializer::new );
		lootModifiers.register( "smelting_items", SmeltingItems.Serializer::new );
	}
}
