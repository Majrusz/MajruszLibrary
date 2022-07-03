package com.mlib.enchantments;

import com.mlib.gamemodifiers.GameModifier;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public abstract class CustomEnchantment extends Enchantment {
	protected final Supplier< Boolean > availability;
	protected final Parameters params;

	protected CustomEnchantment( Supplier< Boolean > availability, Parameters params ) {
		super( params.rarity, params.category, params.slots );

		this.availability = availability;
		this.params = params;
	}

	@Override
	public int getMaxLevel() {
		return this.params.maxLevel;
	}

	@Override
	public int getMinCost( int enchantmentLevel ) {
		return this.params.minLevelFormula.getPlayerLevel( enchantmentLevel );
	}

	@Override
	public int getMaxCost( int enchantmentLevel ) {
		return this.params.maxLevelFormula.getPlayerLevel( enchantmentLevel );
	}

	@Override
	public boolean canEnchant( ItemStack itemStack ) {
		return isEnabled() && super.canEnchant( itemStack );
	}

	@Override
	public boolean isTradeable() {
		return isEnabled() && super.isTradeable();
	}

	@Override
	public boolean isDiscoverable() {
		return isEnabled() && super.isDiscoverable();
	}

	@Override
	public boolean canApplyAtEnchantingTable( ItemStack itemStack ) {
		return isEnabled() && super.canApplyAtEnchantingTable( itemStack );
	}

	@Override
	public boolean isAllowedOnBooks() {
		return isEnabled() && super.isAllowedOnBooks();
	}

	@Override
	public boolean isTreasureOnly() {
		return this.params.isCurse;
	}

	@Override
	public boolean isCurse() {
		return this.params.isCurse;
	}

	public int getEnchantmentLevel( ItemStack itemStack ) {
		return itemStack.getEnchantmentLevel( this );
	}

	public int getEnchantmentLevel( LivingEntity entity ) {
		return EnchantmentHelper.getEnchantmentLevel( this, entity );
	}

	public boolean hasEnchantment( ItemStack itemStack ) {
		return getEnchantmentLevel( itemStack ) > 0;
	}

	public boolean hasEnchantment( LivingEntity entity ) {
		return getEnchantmentLevel( entity ) > 0;
	}

	public boolean increaseEnchantmentLevel( ItemStack itemStack ) {
		int enchantmentLevel = getEnchantmentLevel( itemStack );
		if( enchantmentLevel >= getMaxLevel() ) {
			return false;
		} else if( enchantmentLevel == 0 ) {
			this.enchantItem( itemStack, 1 );
			return true;
		} else {
			this.setEnchantmentTag( itemStack, enchantmentLevel + 1 );
			return true;
		}
	}

	public int getEnchantmentSum( Iterable< ItemStack > itemStacks ) {
		int sum = 0;
		for( ItemStack itemStack : itemStacks )
			sum += this.getEnchantmentLevel( itemStack );

		return sum;
	}

	public int getEnchantmentSum( LivingEntity livingEntity, EquipmentSlot[] slots ) {
		List< ItemStack > itemStackList = new ArrayList<>();
		for( EquipmentSlot slotType : slots )
			itemStackList.add( livingEntity.getItemBySlot( slotType ) );

		return getEnchantmentSum( itemStackList );
	}

	protected boolean isEnabled() {
		return this.availability.get();
	}

	protected boolean isDisabled() {
		return !isEnabled();
	}

	private void enchantItem( ItemStack itemStack, int level ) {
		itemStack.enchant( this, level );
	}

	private void setEnchantmentTag( ItemStack itemStack, int level ) {
		ListTag nbt = itemStack.getEnchantmentTags();
		for( int i = 0; i < nbt.size(); ++i ) {
			CompoundTag enchantmentData = nbt.getCompound( i );
			String enchantmentID = enchantmentData.getString( "id" );

			if( enchantmentID.contains( Registry.ENCHANTMENT.getKey( this ).toString() ) ) {
				enchantmentData.putInt( "lvl", level );
				break;
			}
		}

		itemStack.addTagElement( "Enchantments", nbt );
	}

	public record Parameters( Rarity rarity, EnchantmentCategory category, EquipmentSlot[] slots, boolean isCurse, int maxLevel,
							  LevelFormula minLevelFormula, LevelFormula maxLevelFormula
	) {}

	@FunctionalInterface
	protected interface LevelFormula {
		int getPlayerLevel( int enchantmentLevel );
	}
}
