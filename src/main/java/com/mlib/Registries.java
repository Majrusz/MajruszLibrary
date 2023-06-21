package com.mlib;

import com.mlib.commands.Command;
import com.mlib.entities.EntityHelper;
import com.mlib.features.AnyModification;
import com.mlib.gamemodifiers.Contexts;
import com.mlib.modhelper.ModHelper;
import com.mojang.serialization.Codec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class Registries {
	public static final ModHelper HELPER = ModHelper.create( MajruszLibrary.MOD_ID );

	static final DeferredRegister< Codec< ? extends IGlobalLootModifier > > LOOT_MODIFIERS = HELPER.create( ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS );

	static {
		HELPER.createMessage( EntityHelper.EntityGlow.class, EntityHelper.EntityGlow::new );

		LOOT_MODIFIERS.register( "any_situation", AnyModification.CODEC );

		MinecraftForge.EVENT_BUS.addListener( Command::registerAll );
		FMLJavaModLoadingContext.get().getModEventBus().addListener( Registries::onLoadComplete );
	}

	public static void initialize() {
		HELPER.register();
	}

	private static void onLoadComplete( FMLLoadCompleteEvent event ) {
		Contexts.get().forEach( Contexts::tryToSort );
	}
}
