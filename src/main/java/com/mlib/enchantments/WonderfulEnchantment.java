package com.mlib.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;

/** Class removing redundancy and repetition from enchantments. */
public abstract class WonderfulEnchantment extends Enchantment {
	private static final int DISABLE_ENCHANTMENT_VALUE = 9001;
	private int differenceBetweenMinimumAndMaximum = 10;
	private int maximumEnchantmentLevel = 1;
	private MinimumEnchantabilityCalculator minimumEnchantabilityCalculator = level->level;

	protected WonderfulEnchantment( Rarity rarity, EnchantmentType enchantmentType, EquipmentSlotType[] equipmentSlotTypes ) {
		super( rarity, enchantmentType, equipmentSlotTypes );
	}

	protected WonderfulEnchantment( Rarity rarity, EnchantmentType enchantmentType, EquipmentSlotType equipmentSlotType ) {
		this( rarity, enchantmentType, new EquipmentSlotType[]{ equipmentSlotType } );
	}

	@Override
	public int getMaxLevel() {
		return this.maximumEnchantmentLevel;
	}

	@Override
	public int getMinEnchantability( int level ) {
		return isDisabled() ? DISABLE_ENCHANTMENT_VALUE : this.minimumEnchantabilityCalculator.getMinimumLevel( level );
	}

	@Override
	public int getMaxEnchantability( int level ) {
		return this.getMinEnchantability( level ) + this.differenceBetweenMinimumAndMaximum;
	}

	/** Checks whether the enchantment is disabled. */
	protected abstract boolean isDisabled();

	/**
	 Setting up difference between minimum and maximum enchantability.
	 For example when this value is set to 20 and minimum level is equal 10
	 then player can receive this enchantment only when enchanting with level between 10 and 30.
	 */
	protected void setDifferenceBetweenMinimumAndMaximum( int differenceInLevels ) {
		this.differenceBetweenMinimumAndMaximum = differenceInLevels;
	}

	/**
	 Setting up minimum enchantability calculator.
	 By default in minecraft this is the same as getMinEnchantability() but
	 to work with disabling easily enchantments this needs to be a separate function.
	 */
	protected void setMinimumEnchantabilityCalculator( MinimumEnchantabilityCalculator minimumEnchantabilityCalculator ) {
		this.minimumEnchantabilityCalculator = minimumEnchantabilityCalculator;
	}

	/** Setting up maximum enchantment level this enchantment can have. */
	protected void setMaximumEnchantmentLevel( int enchantmentLevel ) {
		this.maximumEnchantmentLevel = enchantmentLevel;
	}

	@FunctionalInterface
	protected interface MinimumEnchantabilityCalculator {
		int getMinimumLevel( int level );
	}
}
