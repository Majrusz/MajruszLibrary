package com.mlib;

import com.mlib.annotations.AnnotationHandler;
import com.mlib.commands.Command;
import com.mlib.features.AnyModification;
import com.mlib.gamemodifiers.GameModifier;
import com.mlib.registries.RegistryHelper;
import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

import static com.mlib.MajruszLibrary.CONFIG_HANDLER;

public class Registries {
	static final RegistryHelper HELPER = new RegistryHelper( MajruszLibrary.MOD_ID );
	static final DeferredRegister< Codec< ? extends IGlobalLootModifier > > LOOT_MODIFIERS = HELPER.create( ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS );
	static final List< Command > COMMANDS;
	static final List< GameModifier > GAME_MODIFIERS;

	static {
		GameModifier.addNewGroup( CONFIG_HANDLER, GameModifier.DEFAULT_KEY );

		LOOT_MODIFIERS.register( "any_situation", AnyModification.CODEC );

		AnnotationHandler annotationHandler = new AnnotationHandler( MajruszLibrary.MOD_ID );
		COMMANDS = annotationHandler.getInstances( Command.class );
		GAME_MODIFIERS = annotationHandler.getInstances( GameModifier.class );

		MinecraftForge.EVENT_BUS.addListener( Command::registerAll );
		FMLJavaModLoadingContext.get().getModEventBus().addListener( NetworkHandler::register );
	}

	public static void initialize() {
		HELPER.registerAll();
		CONFIG_HANDLER.register( ModLoadingContext.get() );
	}

	public static ResourceLocation getLocation( String register ) {
		return HELPER.getLocation( register );
	}

	public static String getLocationString( String register ) {
		return HELPER.getLocationString( register );
	}
}
