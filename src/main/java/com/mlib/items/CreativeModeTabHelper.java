package com.mlib.items;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;

public class CreativeModeTabHelper {
	public static CreativeModeTab newTab( String label, RegistryObject< ? extends Item > item ) {
		return new CreativeModeTab( label ) {
			@Override
			public ItemStack makeIcon() {
				return new ItemStack( item.get() );
			}
		};
	}
}
