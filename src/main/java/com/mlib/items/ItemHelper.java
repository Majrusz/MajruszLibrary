package com.mlib.items;

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
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

/** Some useful methods for items. */
public class ItemHelper {
	private static final double MINECRAFT_WEAPON_ENCHANT_CHANCE = 0.25;
	private static final double MINECRAFT_ARMOR_PIECE_ENCHANT_CHANCE = 0.5;

	public static int getEnchantmentLevel( double clampedRegionalDifficulty ) {
		return ( int )( 5 + 18 * clampedRegionalDifficulty );
	}

	public static ItemStack damageItem( ItemStack itemStack, double factor ) {
		if( itemStack.getMaxDamage() * factor < 1.0 )
			return ItemStack.EMPTY;

		itemStack.setDamageValue( Random.nextInt( ( int )( itemStack.getMaxDamage() * factor ) ) );
		return itemStack;
	}

	public static ItemStack enchantItem( ItemStack itemStack, double clampedRegionalDifficulty, boolean isTreasureAllowed ) {
		return EnchantmentHelper.enchantItem( Random.getThreadSafe(), itemStack, getEnchantmentLevel( clampedRegionalDifficulty ), isTreasureAllowed );
	}

	public static ItemStack tryEnchantItem( ItemStack itemStack, double clampedRegionalDifficulty, boolean isTreasureAllowed, double chance ) {
		return Random.tryChance( chance ) ? enchantItem( itemStack, clampedRegionalDifficulty, isTreasureAllowed ) : itemStack;
	}

	public static ItemStack tryEnchantWeapon( ItemStack weaponItemStack, double clampedRegionalDifficulty, boolean isTreasureAllowed ) {
		return tryEnchantItem( weaponItemStack, clampedRegionalDifficulty, isTreasureAllowed, MINECRAFT_WEAPON_ENCHANT_CHANCE );
	}

	public static ItemStack tryEnchantArmor( ItemStack armorItemStack, double clampedRegionalDifficulty, boolean isTreasureAllowed ) {
		return tryEnchantItem( armorItemStack, clampedRegionalDifficulty, isTreasureAllowed, MINECRAFT_ARMOR_PIECE_ENCHANT_CHANCE );
	}

	public static ItemStack damageAndEnchantItem( ItemStack itemStack, double clampedRegionalDifficulty, boolean isTreasureAllowed, double damageFactor
	) {
		return damageItem( enchantItem( itemStack, clampedRegionalDifficulty, isTreasureAllowed ), damageFactor );
	}

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

	public static ItemStack getMatchingHandItem( LivingEntity entity, Predicate< ItemStack > predicate ) {
		return predicate.test( entity.getMainHandItem() ) ? entity.getMainHandItem() : entity.getOffhandItem();
	}

	public static void addCooldown( Player player, int duration, Item... items ) {
		ItemCooldowns cooldowns = player.getCooldowns();
		for( Item item : items ) {
			cooldowns.addCooldown( item, duration );
		}
	}

	public static boolean isOnCooldown( Player player, Item... items ) {
		ItemCooldowns cooldowns = player.getCooldowns();
		for( Item item : items ) {
			if( cooldowns.isOnCooldown( item ) )
				return true;
		}

		return false;
	}
}
