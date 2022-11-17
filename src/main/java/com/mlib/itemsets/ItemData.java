package com.mlib.itemsets;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class ItemData {
	final Predicate< ItemStack > condition;
	final Supplier< MutableComponent > name;
	final Supplier< Stream< EquipmentSlot > > equipmentSlots;

	public ItemData( Predicate< ItemStack > condition, Supplier< MutableComponent > name, EquipmentSlot... equipmentSlots ) {
		this.condition = condition;
		this.name = name;
		this.equipmentSlots = ()->Arrays.stream( equipmentSlots );
	}

	public ItemData( Supplier< Item > item, EquipmentSlot... equipmentSlots ) {
		this( itemStack->item.get().equals( itemStack.getItem() ), ()->item.get().getDescription().copy(), equipmentSlots );
	}

	public ItemData( Item item, EquipmentSlot... equipmentSlots ) {
		this( ()->item, equipmentSlots );
	}

	public ItemData( RegistryObject< ? extends Item > item, EquipmentSlot... equipmentSlots ) {
		this( item::get, equipmentSlots );
	}

	public boolean isEquipped( Player player ) {
		return this.equipmentSlots.get().anyMatch( slot->this.matches( player.getItemBySlot( slot ) ) );
	}

	public boolean matches( ItemStack itemStack ) {
		return this.condition.test( itemStack );
	}

	public MutableComponent getTranslatedName() {
		return this.name.get();
	}
}