package com.mlib.items;

import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class ItemHelper {
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
}
