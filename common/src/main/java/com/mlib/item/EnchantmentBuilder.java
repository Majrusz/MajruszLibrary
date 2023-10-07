package com.mlib.item;

import com.mlib.mixin.IMixinEnchantment;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

import java.util.List;

public class EnchantmentBuilder extends Enchantment {
	private boolean isCurse = false;
	private int maxLevel = 1;
	private CostFormula minLevelCost = level->10;
	private CostFormula maxLevelCost = level->50;

	public EnchantmentBuilder() {
		super( null, null, null );
	}

	@Override
	public int getMaxLevel() {
		return this.maxLevel;
	}

	@Override
	public int getMinCost( int enchantmentLevel ) {
		return this.minLevelCost.getLevelCost( enchantmentLevel );
	}

	@Override
	public int getMaxCost( int enchantmentLevel ) {
		return this.maxLevelCost.getLevelCost( enchantmentLevel );
	}

	@Override
	public boolean isTreasureOnly() {
		return this.isCurse;
	}

	@Override
	public boolean isCurse() {
		return this.isCurse;
	}

	public EnchantmentBuilder slots( List< EquipmentSlot > slots ) {
		( ( IMixinEnchantment )this ).setSlots( slots.toArray( EquipmentSlot[]::new ) );

		return this;
	}

	public EnchantmentBuilder rarity( Rarity rarity ) {
		( ( IMixinEnchantment )this ).setRarity( rarity );

		return this;
	}

	public EnchantmentBuilder category( EnchantmentCategory category ) {
		( ( IMixinEnchantment )this ).setCategory( category );

		return this;
	}

	public EnchantmentBuilder curse() {
		this.isCurse = true;

		return this;
	}

	public EnchantmentBuilder maxLevel( int level ) {
		this.maxLevel = level;

		return this;
	}

	public EnchantmentBuilder minLevelCost( CostFormula formula ) {
		this.minLevelCost = formula;

		return this;
	}

	public EnchantmentBuilder maxLevelCost( CostFormula formula ) {
		this.maxLevelCost = formula;

		return this;
	}

	@FunctionalInterface
	protected interface CostFormula {
		int getLevelCost( int level );
	}
}
