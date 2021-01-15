package com.mlib.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;

/** Class removing redundancy and repetition from curses. */
public abstract class ExtendedCurse extends ExtendedEnchantment {
	protected ExtendedCurse( Enchantment.Rarity rarity, EnchantmentType enchantmentType, EquipmentSlotType[] equipmentSlotTypes ) {
		super( rarity, enchantmentType, equipmentSlotTypes );
	}

	protected ExtendedCurse( Enchantment.Rarity rarity, EnchantmentType enchantmentType, EquipmentSlotType equipmentSlotType ) {
		this( rarity, enchantmentType, new EquipmentSlotType[]{ equipmentSlotType } );
	}

	@Override
	public boolean isTreasureEnchantment() {
		return true;
	}

	@Override
	public boolean isCurse() {
		return true;
	}
}
