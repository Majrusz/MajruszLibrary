package com.mlib.items;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;

@Deprecated
public class ItemCreativeModeTab extends CreativeModeTab {
	protected ItemCreativeModeTab( Builder builder ) {
		super( builder );
	}
	/*final RegistryObject< ? extends Item > item;

	public ItemCreativeModeTab( String label, RegistryObject< ? extends Item > item ) {
		super( label );
		this.item = item;
	}

	@Override
	public ItemStack getIconItem() {
		return new ItemStack( this.item.get() );
	}*/
}
