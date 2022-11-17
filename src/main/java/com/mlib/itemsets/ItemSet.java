package com.mlib.itemsets;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

public abstract class ItemSet {
	public static final List< ItemSet > ITEM_SETS = Collections.synchronizedList( new ArrayList<>() );
	final Supplier< Stream< ItemData > > items;
	final ChatFormatting chatFormatting;
	final String keyId;

	public ItemSet( Supplier< Stream< ItemData > > items, ChatFormatting chatFormatting, String keyId ) {
		this.items = items;
		this.chatFormatting = chatFormatting;
		this.keyId = keyId;

		ITEM_SETS.add( this );
	}

	public int countEquippedItems( Player player ) {
		return ( int )this.getItems().filter( item->item.isEquipped( player ) ).count();
	}

	public int getTotalItemsCount() {
		return ( int )this.getItems().count();
	}

	public boolean isPartOfSet( ItemStack itemStack ) {
		return this.getItems().anyMatch( item->item.matches( itemStack ) );
	}

	public Stream< ItemData > getItems() {
		return this.items.get();
	}

	public ChatFormatting getChatFormatting() {
		return this.chatFormatting;
	}

	public MutableComponent getTranslatedName() {
		return Component.translatable( this.keyId );
	}
}
