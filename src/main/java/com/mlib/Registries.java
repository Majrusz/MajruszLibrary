package com.mlib;

import com.mlib.annotations.AnnotationHandler;
import com.mlib.annotations.AutoInstance;
import com.mlib.commands.Command;
import com.mlib.features.BlockSmelter;
import com.mlib.gamemodifiers.GameModifier;
import com.mlib.itemsets.ItemData;
import com.mlib.itemsets.ItemSet;
import com.mlib.loot_modifiers.AnyModification;
import com.mlib.loot_modifiers.HarvestCrop;
import com.mlib.registries.DeferredRegisterHelper;
import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static com.mlib.MajruszLibrary.CONFIG_HANDLER;

public class Registries {
	static final DeferredRegisterHelper HELPER = new DeferredRegisterHelper( MajruszLibrary.MOD_ID );
	static final DeferredRegister< Codec< ? extends IGlobalLootModifier > > LOOT_MODIFIERS = HELPER.create( ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS );
	static final List< Command > COMMANDS;
	static final List< GameModifier > GAME_MODIFIERS;

	static {
		CONFIG_HANDLER.addGroup( GameModifier.addNewGroup( GameModifier.DEFAULT_KEY, "", "" ) );

		LOOT_MODIFIERS.register( "any_situation", AnyModification.CODEC );
		LOOT_MODIFIERS.register( "harvest_crop", HarvestCrop.CODEC );

		COMMANDS = new AnnotationHandler( "com.mlib.commands" ).getInstances( Command.class );
		GAME_MODIFIERS = new AnnotationHandler( "com.mlib.features" ).getInstances( GameModifier.class );
		new TestSet();

		MinecraftForge.EVENT_BUS.addListener( Command::registerAll );
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

	public static class TestSet extends ItemSet {
		static final ItemData ITEM_1 = new ItemData( Items.IRON_HELMET, EquipmentSlot.HEAD );
		static final ItemData ITEM_2 = new ItemData( Items.IRON_CHESTPLATE, EquipmentSlot.CHEST );
		public TestSet() {
			super( ()->Stream.of( ITEM_1, ITEM_2 ) );
		}
	}
}
