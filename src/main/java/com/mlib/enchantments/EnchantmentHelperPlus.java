package com.mlib.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/** Few useful methods to work with enchantments. It is called 'EnchantmentPlusHelper' to not confuse with 'EnchantmentHelper' minecraft class. */
public class EnchantmentHelperPlus {
	/**
	 Counts the sum of all enchantment levels of the entity if given item stack is instance of the type.

	 @param enchantment  Desired enchantment.
	 @param livingEntity Entity to check for item stacks.
	 @param slotTypes    Desired equipment slot types to check.
	 @param type         Required type. (for example ShieldItem.class to only calculate levels of shields)
	 */
	public static < InstanceType > int calculateEnchantmentSumIfIsInstanceOf( Enchantment enchantment, LivingEntity livingEntity,
		EquipmentSlotType[] slotTypes, Class< InstanceType > type
	) {
		int sum = 0;

		for( EquipmentSlotType slotType : slotTypes ) {
			ItemStack itemStack = livingEntity.getItemStackFromSlot( slotType );
			if( type.isInstance( itemStack.getItem() ) )
				sum += EnchantmentHelper.getEnchantmentLevel( enchantment, itemStack );
		}

		return sum;
	}

	/** Counts the sum of all enchantment levels in the specified range if given item stack is instance of the type. */
	public static < InstanceType > int calculateEnchantmentSumIfIsInstanceOf( Enchantment enchantment, Iterable< ItemStack > itemStacks,
		Class< InstanceType > type
	) {
		List< ItemStack > itemStackList = new ArrayList<>();

		for( ItemStack itemStack : itemStacks )
			if( type.isInstance( itemStack.getItem() ) )
				itemStackList.add( itemStack );

		return calculateEnchantmentSum( enchantment, itemStackList );
	}

	/**
	 Counts the sum of all enchantment levels of the entity.

	 @param enchantment  Desired enchantment.
	 @param livingEntity Entity to check for item stacks.
	 @param slotTypes    Desired equipment slot types to check.
	 */
	public static int calculateEnchantmentSum( Enchantment enchantment, LivingEntity livingEntity, EquipmentSlotType[] slotTypes ) {
		List< ItemStack > itemStackList = new ArrayList<>();

		for( EquipmentSlotType slotType : slotTypes )
			itemStackList.add( livingEntity.getItemStackFromSlot( slotType ) );

		return calculateEnchantmentSum( enchantment, itemStackList );
	}

	/** Counts the sum of all enchantment levels in the specified range. */
	public static int calculateEnchantmentSum( Enchantment enchantment, Iterable< ItemStack > itemStacks ) {
		int sum = 0;

		for( ItemStack itemStack : itemStacks )
			sum += EnchantmentHelper.getEnchantmentLevel( enchantment, itemStack );

		return sum;
	}
}
