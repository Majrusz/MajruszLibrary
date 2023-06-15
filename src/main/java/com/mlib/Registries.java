package com.mlib;

import com.mlib.annotations.AnnotationHandler;
import com.mlib.commands.Command;
import com.mlib.features.AnyModification;
import com.mlib.gamemodifiers.Contexts;
import com.mlib.registries.RegistryHelper;
import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class Registries {
	static final RegistryHelper HELPER = new RegistryHelper( MajruszLibrary.MOD_ID );
	static final DeferredRegister< GlobalLootModifierSerializer< ? > > LOOT_MODIFIERS = HELPER.create( ForgeRegistries.Keys.LOOT_MODIFIER_SERIALIZERS );

	static {
		LOOT_MODIFIERS.register( "any_situation", AnyModification.Serializer::new );

		new AnnotationHandler( MajruszLibrary.MOD_ID );

		MinecraftForge.EVENT_BUS.addListener( Command::registerAll );
		final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		modEventBus.addListener( NetworkHandler::register );
		modEventBus.addListener( Registries::onLoadComplete );
	}

	public static void initialize() {
		HELPER.registerAll();
	}

	public static ResourceLocation getLocation( String register ) {
		return HELPER.getLocation( register );
	}

	public static String getLocationString( String register ) {
		return HELPER.getLocationString( register );
	}

	private static void onLoadComplete( FMLLoadCompleteEvent event ) {
		Contexts.get().forEach( Contexts::tryToSort );
	}
}
