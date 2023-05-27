package com.mlib.itemsets;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.*;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

public abstract class ItemSet {
	public static final List< ItemSet > ITEM_SETS = Collections.synchronizedList( new ArrayList<>() );
	final Supplier< Stream< ItemData > > items;
	final Supplier< Stream< BonusData > > bonuses;
	final ChatFormatting chatFormatting;
	final String keyId;

	public ItemSet( Supplier< Stream< ItemData > > items, Supplier< Stream< BonusData > > bonuses, ChatFormatting chatFormatting, String keyId ) {
		this.items = items;
		this.bonuses = bonuses;
		this.chatFormatting = chatFormatting;
		this.keyId = keyId;

		ITEM_SETS.add( this );
	}

	public int countEquippedItems( LivingEntity entity ) {
		return ( int )this.getItems().filter( item->item.isEquipped( entity ) ).count();
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

	public Stream< BonusData > getBonuses() {
		return this.bonuses.get();
	}

	public ChatFormatting getChatFormatting() {
		return this.chatFormatting;
	}

	public MutableComponent getTranslatedName() {
		return new TranslatableComponent( this.keyId );
	}
}
