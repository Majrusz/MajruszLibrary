package com.mlib;

import net.minecraft.inventory.EquipmentSlotType;

/** Equipment slot types grouped together. */
public class EquipmentSlotTypes {
	public static EquipmentSlotType[] ARMOR = new EquipmentSlotType[]{ EquipmentSlotType.HEAD, EquipmentSlotType.CHEST, EquipmentSlotType.LEGS,
		EquipmentSlotType.FEET
	};
	public static EquipmentSlotType[] BOTH_HANDS = new EquipmentSlotType[]{ EquipmentSlotType.MAINHAND, EquipmentSlotType.OFFHAND };
	public static EquipmentSlotType[] ARMOR_AND_HANDS = new EquipmentSlotType[]{ EquipmentSlotType.HEAD, EquipmentSlotType.CHEST,
		EquipmentSlotType.LEGS, EquipmentSlotType.FEET, EquipmentSlotType.MAINHAND, EquipmentSlotType.OFFHAND
	};
}
