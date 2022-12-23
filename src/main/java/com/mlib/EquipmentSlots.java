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
	public static EquipmentSlot[] ALL = new EquipmentSlot[]{
		EquipmentSlot.HEAD,
		EquipmentSlot.CHEST,
		EquipmentSlot.LEGS,
		EquipmentSlot.FEET,
		EquipmentSlot.MAINHAND,
		EquipmentSlot.OFFHAND
	};
	@Deprecated
	public static EquipmentSlot[] ARMOR_AND_HANDS = ALL;
	// below are default values wrapped into arrays
	public static EquipmentSlot[] MAINHAND = new EquipmentSlot[]{ EquipmentSlot.MAINHAND };
	public static EquipmentSlot[] OFFHAND = new EquipmentSlot[]{ EquipmentSlot.OFFHAND };
	public static EquipmentSlot[] HEAD = new EquipmentSlot[]{ EquipmentSlot.HEAD };
	public static EquipmentSlot[] CHEST = new EquipmentSlot[]{ EquipmentSlot.CHEST };
	public static EquipmentSlot[] LEGS = new EquipmentSlot[]{ EquipmentSlot.LEGS };
	public static EquipmentSlot[] FEET = new EquipmentSlot[]{ EquipmentSlot.FEET };
}
