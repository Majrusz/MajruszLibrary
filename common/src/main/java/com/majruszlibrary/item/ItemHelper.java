package com.majruszlibrary.item;

import com.majruszlibrary.entity.EntityHelper;
import com.majruszlibrary.math.Random;
import com.majruszlibrary.math.Range;
import com.majruszlibrary.platform.Services;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class ItemHelper {
	private static final IItemPlatform PLATFORM = Services.load( IItemPlatform.class );
	private static final float MINECRAFT_WEAPON_ENCHANT_CHANCE = 0.25f;
	private static final float MINECRAFT_ARMOR_PIECE_ENCHANT_CHANCE = 0.5f;

	public static Decorator decorate( ItemStack itemStack ) {
		return new Decorator( itemStack );
	}

	public static Optional< SmeltResult > tryToSmelt( Level level, ItemStack itemStack ) {
		Optional< SmeltingRecipe > recipe = level.getRecipeManager().getRecipeFor( RecipeType.SMELTING, new SimpleContainer( itemStack ), level );
		if( recipe.isPresent() ) {
			float experience = recipe.get().getExperience() * itemStack.getCount();
			ItemStack result = recipe.get().getResultItem( level.registryAccess() ).copy();
			result.setCount( result.getCount() * itemStack.getCount() );

			return Optional.of( new SmeltResult( result, experience ) );
		}

		return Optional.empty();
	}

	public static void giveToPlayer( ItemStack itemStack, Player player, Level level ) {
		if( !player.getInventory().add( itemStack ) ) {
			level.addFreshEntity( new ItemEntity( level, player.getX(), player.getY() + 1.0, player.getZ(), itemStack ) );
		}
	}

	public static void damage( LivingEntity entity, EquipmentSlot slot, int damage ) {
		entity.getItemBySlot( slot ).hurtAndBreak( damage, entity, subentity->subentity.broadcastBreakEvent( slot ) );
	}

	public static void damage( LivingEntity entity, InteractionHand hand, int damage ) {
		entity.getItemInHand( hand ).hurtAndBreak( damage, entity, subentity->subentity.broadcastBreakEvent( hand ) );
	}

	/** Required because Mob::equipItemIfPossible makes item a guaranteed drop and enables persistence for mob. */
	public static @Nullable EquipmentSlot equip( Mob mob, ItemStack itemStack ) {
		if( !mob.canHoldItem( itemStack ) ) {
			return null;
		}

		EquipmentSlot equipmentSlot = Mob.getEquipmentSlotForItem( itemStack );
		equipmentSlot = equipmentSlot.isArmor() ? equipmentSlot : EquipmentSlot.MAINHAND;
		mob.setItemSlot( equipmentSlot, itemStack );

		return equipmentSlot;
	}

	public static void consumeItemOnUse( ItemStack itemStack, Player player ) {
		player.awardStat( Stats.ITEM_USED.get( itemStack.getItem() ) );
		if( !EntityHelper.isOnCreativeMode( player ) ) {
			itemStack.shrink( 1 );
		}
	}

	public static void addCooldown( Player player, int duration, Item... items ) {
		for( Item item : items ) {
			player.getCooldowns().addCooldown( item, duration );
		}
	}

	public static < Type extends EntityType< ? extends Mob > > Supplier< SpawnEggItem > createEgg( Supplier< Type > type, int backgroundColor,
		int highlightColor
	) {
		return PLATFORM.createEgg( type, backgroundColor, highlightColor );
	}

	public static ItemStack getHandItem( LivingEntity entity, Predicate< ItemStack > predicate ) {
		return predicate.test( entity.getMainHandItem() ) ? entity.getMainHandItem() : entity.getOffhandItem();
	}

	public static ItemStack getCurrentlyUsedItem( LivingEntity entity ) {
		return entity.isUsingItem() ? entity.getItemInHand( entity.getUsedItemHand() ) : ItemStack.EMPTY;
	}

	public static boolean isOnCooldown( Player player, Item... items ) {
		for( Item item : items ) {
			if( player.getCooldowns().isOnCooldown( item ) ) {
				return true;
			}
		}

		return false;
	}


	public static boolean isShield( Item item ) {
		return item instanceof ShieldItem;
	}

	public static boolean isRangedWeapon( Item item ) {
		return item instanceof BowItem
			|| item instanceof CrossbowItem;
	}

	public static boolean isMeleeWeapon( Item item ) {
		return item instanceof SwordItem
			|| item instanceof TridentItem
			|| item instanceof AxeItem;
	}

	public static boolean isGoldenToolOrArmor( Item item ) {
		return item instanceof SwordItem swordItem && swordItem.getTier() == Tiers.GOLD
			|| item instanceof DiggerItem diggerItem && diggerItem.getTier() == Tiers.GOLD
			|| item instanceof ArmorItem armorItem && armorItem.getMaterial() == ArmorMaterials.GOLD;
	}

	public static boolean isAnyTool( Item item ) {
		return item instanceof SwordItem
			|| item instanceof TridentItem
			|| item instanceof DiggerItem
			|| item instanceof BowItem
			|| item instanceof CrossbowItem;
	}

	public static boolean isFishingRod( Item item ) {
		return item instanceof FishingRodItem;
	}

	public record SmeltResult( ItemStack itemStack, float experience ) {}

	public static class Decorator {
		private final ItemStack itemStack;
		private final List< Runnable > modifications = new ArrayList<>();
		private boolean isTreasureAllowed = false;
		private float enchantChance = 1.0f;

		public Decorator damage( float ratio ) {
			this.modifications.add( ()->{
				if( !this.itemStack.isDamageableItem() ) {
					return;
				}

				this.itemStack.setDamageValue( ( int )( ratio * this.itemStack.getMaxDamage() ) );
			} );

			return this;
		}

		public Decorator damage( Range< Float > ratio ) {
			this.damage( Random.nextFloat( ratio ) );

			return this;
		}

		public Decorator allowTreasureEnchantments() {
			this.isTreasureAllowed = true;

			return this;
		}

		public Decorator withVanillaEnchantmentChance() {
			this.enchantChance = this.itemStack.getItem() instanceof ArmorItem ? MINECRAFT_ARMOR_PIECE_ENCHANT_CHANCE : MINECRAFT_WEAPON_ENCHANT_CHANCE;

			return this;
		}

		public Decorator enchant( double clampedRegionalDifficulty ) {
			this.modifications.add( ()->{
				if( !this.itemStack.isEnchantable() || !Random.check( this.enchantChance ) ) {
					return;
				}

				int level = ( int )( 5 + clampedRegionalDifficulty * Random.nextInt( 18 ) );
				EnchantmentHelper.enchantItem( Random.getThreadSafe(), this.itemStack, level, this.isTreasureAllowed );
			} );

			return this;
		}

		public ItemStack apply() {
			this.modifications.forEach( Runnable::run );

			return this.itemStack;
		}

		private Decorator( ItemStack itemStack ) {
			this.itemStack = itemStack;
		}
	}
}
