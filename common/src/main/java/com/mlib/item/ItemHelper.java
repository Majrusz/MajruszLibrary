package com.mlib.item;

import com.mlib.math.Random;
import com.mlib.math.Range;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ItemHelper {
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
