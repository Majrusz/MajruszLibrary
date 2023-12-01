package com.majruszlibrary.item;

import com.google.common.collect.ImmutableList;
import net.minecraft.world.entity.EquipmentSlot;

import java.util.Arrays;

public class EquipmentSlots {
	public static final ImmutableList< EquipmentSlot > ALL = Arrays.stream( EquipmentSlot.values() ).collect( ImmutableList.toImmutableList() );
	public static final ImmutableList< EquipmentSlot > ARMOR = ImmutableList.of(
		EquipmentSlot.HEAD,
		EquipmentSlot.CHEST,
		EquipmentSlot.LEGS,
		EquipmentSlot.FEET
	);
	public static final ImmutableList< EquipmentSlot > HANDS = ImmutableList.of(
		EquipmentSlot.MAINHAND,
		EquipmentSlot.OFFHAND
	);
	public static final ImmutableList< EquipmentSlot > MAINHAND = ImmutableList.of( EquipmentSlot.MAINHAND );
	public static final ImmutableList< EquipmentSlot > OFFHAND = ImmutableList.of( EquipmentSlot.OFFHAND );
	public static final ImmutableList< EquipmentSlot > HEAD = ImmutableList.of( EquipmentSlot.HEAD );
	public static final ImmutableList< EquipmentSlot > CHEST = ImmutableList.of( EquipmentSlot.CHEST );
	public static final ImmutableList< EquipmentSlot > LEGS = ImmutableList.of( EquipmentSlot.LEGS );
	public static final ImmutableList< EquipmentSlot > FEET = ImmutableList.of( EquipmentSlot.FEET );
}
