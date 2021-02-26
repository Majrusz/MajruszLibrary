package com.mlib.enchantments;

import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;

/** Class removing redundancy and repetition from curses. */
public abstract class ExtendedCurse extends ExtendedEnchantment {
	protected ExtendedCurse( String registerName, Rarity rarity, EnchantmentType enchantmentType, EquipmentSlotType[] equipmentSlotTypes ) {
		super( registerName, rarity, enchantmentType, equipmentSlotTypes );
	}

	protected ExtendedCurse( String registerName, Rarity rarity, EnchantmentType enchantmentType, EquipmentSlotType equipmentSlotType ) {
		this( registerName, rarity, enchantmentType, new EquipmentSlotType[]{ equipmentSlotType } );
	}

	@Deprecated
	protected ExtendedCurse( Rarity rarity, EnchantmentType enchantmentType, EquipmentSlotType[] equipmentSlotTypes ) {
		super( "missing_name", rarity, enchantmentType, equipmentSlotTypes );
	}

	@Deprecated
	protected ExtendedCurse( Rarity rarity, EnchantmentType enchantmentType, EquipmentSlotType equipmentSlotType ) {
		this( "missing_name", rarity, enchantmentType, new EquipmentSlotType[]{ equipmentSlotType } );
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
