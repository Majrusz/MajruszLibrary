package com.mlib.items;

import com.mlib.Utility;
import com.mlib.time.TimeHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class CreativeModeTabHelper {
	public static Supplier< ItemStack > buildMultiIcon( Stream< RegistryObject< ? extends Item > > items, double seconds ) {
		Function< RegistryObject< ? extends Item >, Supplier< ItemStack > > toItemStackSupplier = item->()->new ItemStack( item.get() );
		List< Supplier< ItemStack > > itemStacks = items.map( toItemStackSupplier ).toList();
		return ()->{
			int idx = ( int )( ( TimeHelper.getClientTicks() / Utility.secondsToTicks( seconds ) ) ) % itemStacks.size();

			return itemStacks.get( idx ).get();
		};
	}

	public static Supplier< ItemStack > buildMultiIcon( Stream< RegistryObject< ? extends Item > > items ) {
		return buildMultiIcon( items, 1.25 );
	}
}
