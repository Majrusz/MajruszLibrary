package com.mlib.items;

import com.mlib.MajruszLibrary;
import com.mlib.Random;
import com.mlib.entities.EntityHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.jetbrains.annotations.Nullable;

/** Some useful methods for items. */
public class ItemHelper {
	private static final double MINECRAFT_WEAPON_ENCHANT_CHANCE = 0.25;
	private static final double MINECRAFT_ARMOR_PIECE_ENCHANT_CHANCE = 0.5;

	/** Returns enchantment level based on clamped regional difficulty. */
	public static int getEnchantmentLevel( double clampedRegionalDifficulty ) {
		return ( int )( 5 + 18 * clampedRegionalDifficulty );
	}

	/**
	 Returns damaged item.

	 @param itemStack Item stack to damage.
	 @param factor    Factor from range [0.0;1.0] how much item should be damaged at most.

	 @return Returns the same item stack but damaged. (not copy)
	 */
	public static ItemStack damageItem( ItemStack itemStack, double factor ) {
		if( itemStack.getMaxDamage() * factor < 1.0 )
			return ItemStack.EMPTY;

		itemStack.setDamageValue( MajruszLibrary.RANDOM.nextInt( ( int )( itemStack.getMaxDamage() * factor ) ) );
		return itemStack;
	}

	/**
	 Enchants item stack with random enchantments.

	 @param itemStack                 Item stack to enchant.
	 @param clampedRegionalDifficulty Factor at which enchantment level depends.
	 @param isTreasureAllowed         Flag responsible for enabling enchantment like Frost Walker.

	 @return Returns the same item stack but enchanted. (not copy)
	 */
	public static ItemStack enchantItem( ItemStack itemStack, double clampedRegionalDifficulty, boolean isTreasureAllowed ) {
		int enchantmentLevel = getEnchantmentLevel( clampedRegionalDifficulty );

		return EnchantmentHelper.enchantItem( MajruszLibrary.RANDOM, itemStack, enchantmentLevel, isTreasureAllowed );
	}

	/**
	 Have a given chance to enchant an item stack.

	 @param itemStack                 Item stack to enchant.
	 @param clampedRegionalDifficulty Factor at which enchantment level depends.
	 @param isTreasureAllowed         Flag responsible for enabling enchantment like Frost Walker.
	 @param chance                    Chance for enchanting item stack.

	 @return Returns the same item stack but enchanted if drawing chance succeeded or just item stack otherwise. (not copy)
	 */
	public static ItemStack tryEnchantItem( ItemStack itemStack, double clampedRegionalDifficulty, boolean isTreasureAllowed, double chance ) {
		return Random.tryChance( chance ) ? enchantItem( itemStack, clampedRegionalDifficulty, isTreasureAllowed ) : itemStack;
	}

	/**
	 Have a fixed chance to enchant a weapon item stack.

	 @param weaponItemStack           Weapon item stack to enchant.
	 @param clampedRegionalDifficulty Factor at which enchantment level depends.
	 @param isTreasureAllowed         Flag responsible for enabling enchantment like Frost Walker.

	 @return Returns the same item stack but enchanted if drawing chance succeeded or just item stack otherwise. (not copy)
	 */
	public static ItemStack tryEnchantWeapon( ItemStack weaponItemStack, double clampedRegionalDifficulty, boolean isTreasureAllowed ) {
		return tryEnchantItem( weaponItemStack, clampedRegionalDifficulty, isTreasureAllowed, MINECRAFT_WEAPON_ENCHANT_CHANCE );
	}

	/**
	 Have a fixed chance to enchant an armor item stack.

	 @param armorItemStack            Armor item stack to enchant.
	 @param clampedRegionalDifficulty Factor at which enchantment level depends.
	 @param isTreasureAllowed         Flag responsible for enabling enchantment like Frost Walker.

	 @return Returns the same item stack but enchanted if drawing chance succeeded or just item stack otherwise. (not copy)
	 */
	public static ItemStack tryEnchantArmor( ItemStack armorItemStack, double clampedRegionalDifficulty, boolean isTreasureAllowed ) {
		return tryEnchantItem( armorItemStack, clampedRegionalDifficulty, isTreasureAllowed, MINECRAFT_ARMOR_PIECE_ENCHANT_CHANCE );
	}

	/**
	 Enchanting and damaging given item stack.

	 @param itemStack                 Item stack to enchant and damage.
	 @param clampedRegionalDifficulty Factor at which enchantment level depends.
	 @param isTreasureAllowed         Flag responsible for enabling enchantment like Frost Walker.
	 @param damageFactor              Factor from range [0.0;1.0] how much item should be damaged at most.
	 */
	public static ItemStack damageAndEnchantItem( ItemStack itemStack, double clampedRegionalDifficulty, boolean isTreasureAllowed, double damageFactor
	) {
		return damageItem( enchantItem( itemStack, clampedRegionalDifficulty, isTreasureAllowed ), damageFactor );
	}

	/**
	 Adds enchantment type to item group. (every new created enchantment of this type will automatically be added to item group)
	 It is only required to call when you are adding new enchantment type!
	 */
	public static void addEnchantmentTypeToItemGroup( CreativeModeTab itemTab, EnchantmentCategory category ) {
		EnchantmentCategory[] group = itemTab.getEnchantmentCategories();
		if( group.length == 0 ) {
			itemTab.setEnchantmentCategories( category );
			return;
		}
		EnchantmentCategory[] temporary = new EnchantmentCategory[ group.length + 1 ];
		System.arraycopy( group, 0, temporary, 0, group.length );
		temporary[ group.length - 1 ] = category;
		itemTab.setEnchantmentCategories( temporary );
	}

	/**
	 Adds enchantment types to item group. (every new created enchantment of this type will automatically be added to item group)
	 It is only required to call when you are adding new enchantment type!
	 */
	public static void addEnchantmentTypesToItemGroup( CreativeModeTab itemTab, EnchantmentCategory... categories ) {
		EnchantmentCategory[] group = itemTab.getEnchantmentCategories();
		if( group.length == 0 ) {
			itemTab.setEnchantmentCategories( categories );
		} else {
			EnchantmentCategory[] temporary = new EnchantmentCategory[ group.length + categories.length ];
			System.arraycopy( group, 0, temporary, 0, group.length );
			int i = 1;
			for( EnchantmentCategory category : categories ) {
				temporary[ group.length - i ] = category;
				i++;
			}
			itemTab.setEnchantmentCategories( temporary );
		}
	}

	/** Gives item stack to player. If possible gives to inventory otherwise spawns in the level near the player. */
	public static void giveItemStackToPlayer( ItemStack itemStack, Player player, ServerLevel level ) {
		if( !player.getInventory().add( itemStack ) ) {
			double x = player.getX(), y = player.getY() + 1.0, z = player.getZ();
			level.addFreshEntity( new ItemEntity( level, x, y, z, itemStack ) );
		}
	}

	public static void consumeItemOnUse( ItemStack itemStack, Player player ) {
		player.awardStat( Stats.ITEM_USED.get( itemStack.getItem() ) );
		if( !EntityHelper.isOnCreativeMode( player ) ) {
			itemStack.shrink( 1 );
		}
	}

	@SafeVarargs
	public static boolean has( @Nullable LivingEntity entity, EquipmentSlot equipmentSlot, Class< ? extends Item >... itemClasses ) {
		Item item = entity != null ? entity.getItemBySlot( equipmentSlot ).getItem() : null;

		for( Class< ? extends Item > itemClass : itemClasses ) {
			if( itemClass.isInstance( item ) ) {
				return true;
			}
		}
		return false;
	}

	@SafeVarargs
	public static boolean hasInMainHand( @Nullable LivingEntity entity, Class< ? extends Item >... itemClasses ) {
		return has( entity, EquipmentSlot.MAINHAND, itemClasses );
	}

	public static ItemStack getCurrentlyUsedItem( LivingEntity entity ) {
		return entity.isUsingItem() ? entity.getItemInHand( entity.getUsedItemHand() ) : ItemStack.EMPTY;
	}
}
