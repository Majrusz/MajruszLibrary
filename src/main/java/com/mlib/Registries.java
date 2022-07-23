package com.mlib;

import com.mlib.gamemodifiers.GameModifier;
import com.mlib.loot_modifiers.AnyModification;
import com.mlib.loot_modifiers.HarvestCrop;
import com.mlib.features.BlockSmelter;
import com.mlib.registries.DeferredRegisterHelper;
import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class Registries {
	static final DeferredRegisterHelper HELPER = new DeferredRegisterHelper( MajruszLibrary.MOD_ID );
	static final DeferredRegister< Codec< ? extends IGlobalLootModifier > > LOOT_MODIFIERS = HELPER.create( ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS );
	static final List< GameModifier > GAME_MODIFIERS = new ArrayList<>();

	static {
		LOOT_MODIFIERS.register( "any_situation", AnyModification.CODEC );
		LOOT_MODIFIERS.register( "harvest_crop", HarvestCrop.CODEC );

		GAME_MODIFIERS.add( new BlockSmelter() );
	}

	public static void initialize() {
		HELPER.registerAll();
		MajruszLibrary.CONFIG_HANDLER.register( ModLoadingContext.get() );
	}

	public static ResourceLocation getLocation( String register ) {
		return HELPER.getLocation( register );
	}
}
