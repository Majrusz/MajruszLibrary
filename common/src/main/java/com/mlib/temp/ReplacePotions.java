package com.mlib.temp;

import com.mlib.MajruszLibrary;
import com.mlib.annotation.AutoInstance;
import com.mlib.contexts.OnItemBrewed;
import com.mlib.contexts.OnItemDecorationsRendered;
import com.mlib.contexts.base.Condition;
import com.mlib.platform.Side;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

@AutoInstance
public class ReplacePotions {
	public ReplacePotions() {
		OnItemBrewed.listen( data->data.mapPotions( itemStacks->itemStacks.stream().map( itemStack->{
			return itemStack.isEmpty() ? itemStack : new ItemStack( Items.APPLE );
		} ).toList() ) );

		Side.runOnClient( ()->()->{
			OnItemDecorationsRendered.listen( this::renderDecorations )
				.addCondition( Condition.predicate( data->data.itemStack.is( Items.APPLE ) ) );
		} );
	}

	private void renderDecorations( OnItemDecorationsRendered data ) {
		data.gui.renderItem( new ItemStack( MajruszLibrary.THE_ITEM.get() ), data.x, data.y );
	}
}
