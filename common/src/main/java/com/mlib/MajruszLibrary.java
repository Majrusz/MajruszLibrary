package com.mlib;

import com.mlib.command.EnumArgument;
import com.mlib.modhelper.ModHelper;
import com.mlib.network.NetworkObject;
import com.mlib.platform.ISidePlatform;
import com.mlib.platform.Services;
import com.mlib.registry.RegistryGroup;
import com.mlib.registry.RegistryObject;
import com.mlib.temp.TestCommand;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class MajruszLibrary {
	public static final String MOD_ID = "mlib";
	public static final ModHelper HELPER = ModHelper.create( MOD_ID );
	public static final ISidePlatform SIDE = Services.load( ISidePlatform.class );

	// Registry Groups
	public static final RegistryGroup< ArgumentTypeInfo< ?, ? > > ARGUMENT_TYPES = HELPER.create( BuiltInRegistries.COMMAND_ARGUMENT_TYPE );
	public static final RegistryGroup< Item > ITEMS = HELPER.create( BuiltInRegistries.ITEM );
	public static final RegistryGroup< CreativeModeTab > CREATIVE_MODE_TABS = HELPER.create( BuiltInRegistries.CREATIVE_MODE_TAB );

	// Registry Objects
	public static final RegistryObject< ArgumentTypeInfo< ?, ? > > ENUM_ARGUMENT_TYPE = ARGUMENT_TYPES.create( "enum", EnumArgument::register );

	// Items
	public static final RegistryObject< Item > THE_ITEM = ITEMS.create( "the_item", ()->new Item( new Item.Properties() ) );

	// Creative Mode Tabs
	public static final RegistryObject< CreativeModeTab > THE_TAB = CREATIVE_MODE_TABS.create( "the_tab", ()->{
		return CreativeModeTab.builder( CreativeModeTab.Row.TOP, 0 )
			// .withTabsBefore( ResourceKey.create( Registries.CREATIVE_MODE_TAB, new ResourceLocation( "spawn_eggs" ) ) )
			.title( Component.translatable( "itemGroup.mlib.the_tab" ) )
			.icon( ()->new ItemStack( THE_ITEM.get() ) )
			.displayItems( ( params, output )->output.accept( new ItemStack( THE_ITEM.get() ) ) )
			.build();
	} );

	// Network
	public static final NetworkObject< TestCommand.Message > MESSAGE = HELPER.create( "message", TestCommand.Message.class );

	private MajruszLibrary() {}
}
