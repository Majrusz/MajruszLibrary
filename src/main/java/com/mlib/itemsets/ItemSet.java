package com.mlib.itemsets;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

public abstract class ItemSet {
	public static final List< ItemSet > ITEM_SETS = Collections.synchronizedList( new ArrayList<>() );
	protected final Supplier< Stream< ItemData > > items;

	public ItemSet( Supplier< Stream< ItemData > > items ) {
		this.items = items;

		ITEM_SETS.add( this );
	}

	public int countEquippedItems( Player player ) {
		return ( int )this.getItems().filter( item->item.isEquipped( player ) ).count();
	}

	public boolean isPartOfSet( ItemStack itemStack ) {
		return this.getItems().anyMatch( item->item.matches( itemStack ) );
	}

	public Stream< ItemData > getItems() {
		return this.items.get();
	}
}
