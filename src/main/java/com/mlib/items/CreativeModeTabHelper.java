package com.mlib.items;

import com.mlib.Utility;
import com.mlib.time.TimeHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class CreativeModeTabHelper {
	public static Consumer< Consumer< CreativeModeTab.Builder > > newTab( FMLJavaModLoadingContext context,
		ResourceLocation location
	) {
		return builder->{
			context.getModEventBus().addListener( ( CreativeModeTabEvent.Register event )->{
				event.registerCreativeModeTab( location, builder );
			} );
		};
	}

	public static Supplier< ItemStack > buildMultiIcon( Stream< RegistryObject< ? extends Item > > items, double seconds ) {
		Function< RegistryObject< ? extends Item >, Supplier< ItemStack > > toItemStackSupplier = item->()->new ItemStack( item.get() );
		List< Supplier< ItemStack > > itemStacks = items.map( toItemStackSupplier ).toList();
		return ()->{
			int idx = ( int )( ( TimeHelper.getClientTicks() / Utility.secondsToTicks( seconds ) ) ) % itemStacks.size();

			return itemStacks.get( idx ).get();
		};
	}

	public static Supplier< ItemStack > buildMultiIcon( Stream< RegistryObject< ? extends Item > > items ) {
		return buildMultiIcon( items, 2.0 );
	}
}
