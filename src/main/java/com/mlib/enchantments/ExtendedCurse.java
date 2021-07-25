package com.mlib.enchantments;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

/** Class removing redundancy and repetition from curses. */
public abstract class ExtendedCurse extends ExtendedEnchantment {
	protected ExtendedCurse( String registerName, Rarity rarity, EnchantmentCategory enchantmentCategory, EquipmentSlot[] equipmentSlots ) {
		super( registerName, rarity, enchantmentCategory, equipmentSlots );
	}

	protected ExtendedCurse( String registerName, Rarity rarity, EnchantmentCategory enchantmentCategory, EquipmentSlot equipmentSlot ) {
		this( registerName, rarity, enchantmentCategory, new EquipmentSlot[]{ equipmentSlot } );
	}

	@Override
	public boolean isTreasureOnly() {
		return true;
	}

	@Override
	public boolean isCurse() {
		return true;
	}
}
