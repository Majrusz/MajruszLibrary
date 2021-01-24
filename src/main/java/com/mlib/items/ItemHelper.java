package com.mlib.items;

import com.mlib.MajruszLibrary;
import com.mlib.Random;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.world.server.ServerWorld;

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
		itemStack.setDamage( MajruszLibrary.RANDOM.nextInt( ( int )( itemStack.getMaxDamage() * factor ) ) );

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

		return EnchantmentHelper.addRandomEnchantment( MajruszLibrary.RANDOM, itemStack, enchantmentLevel, isTreasureAllowed );
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
	public static ItemStack damageAndEnchantItem( ItemStack itemStack, double clampedRegionalDifficulty, boolean isTreasureAllowed,
		double damageFactor
	) {
		return damageItem( enchantItem( itemStack, clampedRegionalDifficulty, isTreasureAllowed ), damageFactor );
	}

	/**
	 Adds enchantment type to item group. (every new created enchantment of this type will automatically be added to item group)
	 It is only required to call when you are adding new enchantment type!
	 */
	public static void addEnchantmentTypeToItemGroup( EnchantmentType type, ItemGroup itemGroup ) {
		EnchantmentType[] group = itemGroup.getRelevantEnchantmentTypes();
		if( group.length == 0 ) {
			itemGroup.setRelevantEnchantmentTypes( type );
			return;
		}
		EnchantmentType[] temporary = new EnchantmentType[ group.length + 1 ];
		System.arraycopy( group, 0, temporary, 0, group.length );
		temporary[ group.length - 1 ] = type;
		itemGroup.setRelevantEnchantmentTypes( temporary );
	}

	/**
	 Adds enchantment types to item group. (every new created enchantment of this type will automatically be added to item group)
	 It is only required to call when you are adding new enchantment type!
	 */
	public static void addEnchantmentTypesToItemGroup( ItemGroup itemGroup, EnchantmentType ...types ) {
		EnchantmentType[] group = itemGroup.getRelevantEnchantmentTypes();
		if( group.length == 0 ) {
			itemGroup.setRelevantEnchantmentTypes( types );
		} else {
			EnchantmentType[] temporary = new EnchantmentType[ group.length + types.length ];
			System.arraycopy( group, 0, temporary, 0, group.length );
			int i = 1;
			for( EnchantmentType enchantmentType : types ) {
				temporary[ group.length - i ] = enchantmentType;
				i++;
			}
			itemGroup.setRelevantEnchantmentTypes( temporary );
		}
	}

	/**
	 Gives item stack to player. If possible gives to inventory otherwise spawns in the world near the player.

	 @param itemStack Item stack to give.
	 @param player    Player to receive item stack.
	 @param world     World where item should spawn if it is not possible to give item directly.
	 */
	public static void giveItemStackToPlayer( ItemStack itemStack, PlayerEntity player, ServerWorld world ) {
		if( !player.inventory.addItemStackToInventory( itemStack ) ) {
			double x = player.getPosX(), y = player.getPosY() + 1.0, z = player.getPosZ();
			world.addEntity( new ItemEntity( world, x, y, z, itemStack ) );
		}
	}
}
