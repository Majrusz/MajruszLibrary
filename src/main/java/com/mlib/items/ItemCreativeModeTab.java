package com.mlib.items;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;

public class ItemCreativeModeTab extends CreativeModeTab {
	final RegistryObject< Item > item;

	public ItemCreativeModeTab( String label, RegistryObject< Item > item ) {
		super( label );
		this.item = item;
	}

	@Override
	public ItemStack makeIcon() {
		return new ItemStack( this.item.get() );
	}
}
