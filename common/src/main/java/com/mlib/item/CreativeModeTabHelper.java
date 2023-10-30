package com.mlib.item;

import com.mlib.contexts.OnCreativeModeTabIconGet;
import com.mlib.time.TimeHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class CreativeModeTabHelper {
	public static void createItemIconReplacer( Supplier< List< Item > > supplier, Component title ) {
		CreativeModeTabHelper.createItemStackIconReplacer( ()->supplier.get().stream().map( ItemStack::new ).toList(), title );
	}

	public static void createItemStackIconReplacer( Supplier< List< ItemStack > > supplier, Component title ) {
		List< ItemStack > itemStacks = new ArrayList<>();

		OnCreativeModeTabIconGet.listen( data->{
			if( data.title == title ) {
				if( itemStacks.isEmpty() ) {
					itemStacks.addAll( supplier.get() );
				}

				data.icon = itemStacks.get( ( int )( ( TimeHelper.getTicks() / TimeHelper.toTicks( 1.5 ) ) ) % itemStacks.size() );
			}
		} );
	}
}
