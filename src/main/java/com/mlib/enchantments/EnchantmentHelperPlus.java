package com.mlib.enchantments;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

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
		EquipmentSlot[] slotTypes, Class< InstanceType > type
	) {
		int sum = 0;

		for( EquipmentSlot slotType : slotTypes ) {
			ItemStack itemStack = livingEntity.getItemBySlot( slotType );
			if( type.isInstance( itemStack.getItem() ) )
				sum += EnchantmentHelper.getItemEnchantmentLevel( enchantment, itemStack );
		}

		return sum;
	}

	/** Counts the sum of all enchantment levels in the specified range if given item stack is instance of the type. */
	public static < InstanceType > int calculateEnchantmentSumIfIsInstanceOf( Enchantment enchantment, Iterable< ItemStack > itemStacks,
		Class< InstanceType > type
	) {
		List< ItemStack > itemStackList = new ArrayList<>();

		if( itemStacks != null )
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
	public static int calculateEnchantmentSum( Enchantment enchantment, LivingEntity livingEntity, EquipmentSlot[] slotTypes ) {
		List< ItemStack > itemStackList = new ArrayList<>();

		for( EquipmentSlot slotType : slotTypes )
			itemStackList.add( livingEntity.getItemBySlot( slotType ) );

		return calculateEnchantmentSum( enchantment, itemStackList );
	}

	/** Counts the sum of all enchantment levels in the specified range. */
	public static int calculateEnchantmentSum( Enchantment enchantment, Iterable< ItemStack > itemStacks ) {
		int sum = 0;

		if( itemStacks != null )
			for( ItemStack itemStack : itemStacks )
				sum += EnchantmentHelper.getItemEnchantmentLevel( enchantment, itemStack );

		return sum;
	}
}
