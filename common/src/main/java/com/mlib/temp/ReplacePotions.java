package com.mlib.temp;

import com.mlib.annotation.AutoInstance;
import com.mlib.contexts.OnItemBrewed;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

@AutoInstance
public class ReplacePotions {
	public ReplacePotions() {
		OnItemBrewed.listen( data->data.mapPotions( itemStacks->itemStacks.stream().map( itemStack->{
			return itemStack.isEmpty() ? itemStack : new ItemStack( Items.APPLE );
		} ).toList() ) );
	}
}
