package com.mlib;

import net.minecraft.world.entity.EquipmentSlot;

public class EquipmentSlots {
	public static EquipmentSlot[] ARMOR = new EquipmentSlot[]{
		EquipmentSlot.HEAD,
		EquipmentSlot.CHEST,
		EquipmentSlot.LEGS,
		EquipmentSlot.FEET
	};
	public static EquipmentSlot[] BOTH_HANDS = new EquipmentSlot[]{
		EquipmentSlot.MAINHAND,
		EquipmentSlot.OFFHAND
	};
	public static EquipmentSlot[] ARMOR_AND_HANDS = new EquipmentSlot[]{
		EquipmentSlot.HEAD,
		EquipmentSlot.CHEST,
		EquipmentSlot.LEGS,
		EquipmentSlot.FEET,
		EquipmentSlot.MAINHAND,
		EquipmentSlot.OFFHAND
	};
}
