package com.mlib;

import com.mlib.command.EnumArgument;
import com.mlib.entity.EntityHelper;
import com.mlib.item.EquipmentSlots;
import com.mlib.item.EnchantmentBuilder;
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
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class MajruszLibrary {
	public static final String MOD_ID = "mlib";
	public static final ModHelper HELPER = ModHelper.create( MOD_ID );
	public static final ISidePlatform SIDE = Services.load( ISidePlatform.class );

	// Registry Groups
	public static final RegistryGroup< ArgumentTypeInfo< ?, ? > > ARGUMENT_TYPES = HELPER.create( BuiltInRegistries.COMMAND_ARGUMENT_TYPE );
	public static final RegistryGroup< Item > ITEMS = HELPER.create( BuiltInRegistries.ITEM );
	public static final RegistryGroup< CreativeModeTab > CREATIVE_MODE_TABS = HELPER.create( BuiltInRegistries.CREATIVE_MODE_TAB );
	public static final RegistryGroup< Enchantment > ENCHANTMENTS = HELPER.create( BuiltInRegistries.ENCHANTMENT );

	// Registry Objects
	public static final RegistryObject< ArgumentTypeInfo< ?, ? > > ENUM_ARGUMENT_TYPE = ARGUMENT_TYPES.create( "enum", EnumArgument::register );
	public static final RegistryObject< Item > THE_ITEM = ITEMS.create( "the_item", ()->new Item( new Item.Properties() ) );
	public static final RegistryObject< CreativeModeTab > THE_TAB = CREATIVE_MODE_TABS.create( "the_tab", ()->{
		return CreativeModeTab.builder( CreativeModeTab.Row.TOP, 0 )
			// .withTabsBefore( ResourceKey.create( Registries.CREATIVE_MODE_TAB, new ResourceLocation( "spawn_eggs" ) ) )
			.title( Component.translatable( "itemGroup.mlib.the_tab" ) )
			.icon( ()->new ItemStack( THE_ITEM.get() ) )
			.displayItems( ( params, output )->output.accept( new ItemStack( THE_ITEM.get() ) ) )
			.build();
	} );
	public static final RegistryObject< Enchantment > THE_ENCHANTMENT = ENCHANTMENTS.create( "the_enchantment", ()->new EnchantmentBuilder()
		.category( EnchantmentCategory.BOW )
		.slots( EquipmentSlots.HANDS )
		.rarity( Enchantment.Rarity.RARE )
	);

	// Network
	public static final NetworkObject< TestCommand.Message > MESSAGE = HELPER.create( "message", TestCommand.Message.class );
	public static final NetworkObject< EntityHelper.EntityGlow > ENTITY_GLOW = HELPER.create( "entity_glow", EntityHelper.EntityGlow.class );

	private MajruszLibrary() {}
}
