package com.mlib;

import com.mlib.features.BlockSmelter;
import com.mlib.gamemodifiers.GameModifier;
import com.mlib.loot_modifiers.AnyModification;
import com.mlib.loot_modifiers.HarvestCrop;
import com.mlib.registries.DeferredRegisterHelper;
import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

import static com.mlib.MajruszLibrary.CONFIG_HANDLER;

public class Registries {
	static final DeferredRegisterHelper HELPER = new DeferredRegisterHelper( MajruszLibrary.MOD_ID );
	static final DeferredRegister< GlobalLootModifierSerializer< ? > > LOOT_MODIFIERS = HELPER.create( ForgeRegistries.Keys.LOOT_MODIFIER_SERIALIZERS );
	static final List< GameModifier > GAME_MODIFIERS = new ArrayList<>();

	static {
		CONFIG_HANDLER.addGroup( GameModifier.addNewGroup( GameModifier.DEFAULT_KEY, "", "" ) );

		LOOT_MODIFIERS.register( "any_situation", AnyModification.Serializer::new );
		LOOT_MODIFIERS.register( "harvest_crop", HarvestCrop.Serializer::new );

		GAME_MODIFIERS.add( new BlockSmelter() );
	}

	public static void initialize() {
		HELPER.registerAll();
		CONFIG_HANDLER.register( ModLoadingContext.get() );
	}

	public static ResourceLocation getLocation( String register ) {
		return HELPER.getLocation( register );
	}

	public static String getLocationString( String register ) {
		return getLocation( register ).toString();
	}
}
