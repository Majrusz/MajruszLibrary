package com.mlib.enchantments;

import com.mlib.ObfuscationGetter;
import com.mlib.Utility;
import com.mlib.items.ItemHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

public abstract class CustomEnchantment extends Enchantment {
	static final ObfuscationGetter.Field< Enchantment, EquipmentSlot[] > SLOTS = new ObfuscationGetter.Field<>( Enchantment.class, "f_44671_" );
	static final ObfuscationGetter.Field< Enchantment, Rarity > RARITY = new ObfuscationGetter.Field<>( Enchantment.class, "f_44674_" );
	static final ObfuscationGetter.Field< Enchantment, EnchantmentCategory > CATEGORY = new ObfuscationGetter.Field<>( Enchantment.class, "f_44672_" );
	protected Supplier< Boolean > availability = ()->true;
	protected boolean isCurse = false;
	protected int maxLevel = 1;
	protected CostFormula minLevelCost = level->10;
	protected CostFormula maxLevelCost = level->50;

	protected CustomEnchantment() {
		super( null, null, null );
	}

	@Override
	public int getMaxLevel() {
		return this.maxLevel;
	}

	@Override
	public int getMinCost( int enchantmentLevel ) {
		return this.minLevelCost.getCostLevel( enchantmentLevel );
	}

	@Override
	public int getMaxCost( int enchantmentLevel ) {
		return this.maxLevelCost.getCostLevel( enchantmentLevel );
	}

	@Override
	public boolean canEnchant( ItemStack itemStack ) {
		return this.isEnabled() && super.canEnchant( itemStack );
	}

	@Override
	public boolean isTradeable() {
		return this.isEnabled() && super.isTradeable();
	}

	@Override
	public boolean isDiscoverable() {
		return this.isEnabled() && super.isDiscoverable();
	}

	@Override
	public boolean canApplyAtEnchantingTable( ItemStack itemStack ) {
		return this.isEnabled() && super.canApplyAtEnchantingTable( itemStack );
	}

	@Override
	public boolean isAllowedOnBooks() {
		return this.isEnabled() && super.isAllowedOnBooks();
	}

	@Override
	public boolean isTreasureOnly() {
		return this.isCurse;
	}

	@Override
	public boolean isCurse() {
		return this.isCurse;
	}

	public CustomEnchantment slots( EquipmentSlot[] slots ) {
		SLOTS.set( this, slots );

		return this;
	}

	public CustomEnchantment rarity( Rarity rarity ) {
		RARITY.set( this, rarity );

		return this;
	}

	public CustomEnchantment category( EnchantmentCategory category ) {
		CATEGORY.set( this, category );

		return this;
	}

	public CustomEnchantment curse() {
		this.isCurse = true;

		return this;
	}

	public CustomEnchantment maxLevel( int level ) {
		this.maxLevel = level;

		return this;
	}

	public CustomEnchantment minLevelCost( CostFormula formula ) {
		this.minLevelCost = formula;

		return this;
	}

	public CustomEnchantment maxLevelCost( CostFormula formula ) {
		this.maxLevelCost = formula;

		return this;
	}

	public int getEnchantmentLevel( ItemStack itemStack ) {
		return EnchantmentHelper.getItemEnchantmentLevel( this, itemStack );
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

	public boolean removeEnchantment( ItemStack itemStack ) {
		if( this.getEnchantmentLevel( itemStack ) == 0 )
			return false;

		ListTag nbt = itemStack.getEnchantmentTags();
		for( int i = 0; i < nbt.size(); ++i ) {
			CompoundTag enchantmentData = nbt.getCompound( i );
			if( enchantmentData.getString( "id" ).contains( Utility.getRegistryString( this ) ) ) {
				nbt.remove( i );
				break;
			}
		}

		itemStack.addTagElement( "Enchantments", nbt );
		return true;
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

	public int getMatchingHandItemEnchantmentLevel( LivingEntity entity, Predicate< ItemStack > predicate ) {
		return getEnchantmentLevel( ItemHelper.getMatchingHandItem( entity, predicate ) );
	}

	public ItemStack deduceUsedHandItem( LivingEntity entity ) {
		return ItemHelper.getMatchingHandItem( entity, itemStack->this.category.canEnchant( itemStack.getItem() ) );
	}

	public int getDeducedHandEnchantmentLevel( LivingEntity entity ) {
		return getEnchantmentLevel( deduceUsedHandItem( entity ) );
	}

	public void setEnabledSupplier( Supplier< Boolean > availability ) {
		this.availability = availability;
	}

	protected boolean isEnabled() {
		return this.availability.get();
	}

	protected boolean isDisabled() {
		return !this.isEnabled();
	}

	private void enchantItem( ItemStack itemStack, int level ) {
		itemStack.enchant( this, level );
	}

	private void setEnchantmentTag( ItemStack itemStack, int level ) {
		ListTag nbt = itemStack.getEnchantmentTags();
		for( int i = 0; i < nbt.size(); ++i ) {
			CompoundTag enchantmentData = nbt.getCompound( i );
			String enchantmentID = enchantmentData.getString( "id" );

			if( enchantmentID.contains( ForgeRegistries.ENCHANTMENTS.getKey( this ).toString() ) ) {
				enchantmentData.putInt( "lvl", level );
				break;
			}
		}

		itemStack.addTagElement( "Enchantments", nbt );
	}

	@FunctionalInterface
	protected interface CostFormula {
		int getCostLevel( int enchantmentLevel );
	}
}
