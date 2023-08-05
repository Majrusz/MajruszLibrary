package com.mlib.features;

import com.mlib.Random;
import com.mlib.annotations.AutoInstance;
import com.mlib.effects.ParticleHandler;
import com.mlib.entities.EntityHelper;
import com.mlib.contexts.base.Condition;
import com.mlib.contexts.base.Priority;
import com.mlib.contexts.OnBlockSmeltCheck;
import com.mlib.contexts.OnLoot;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.List;
import java.util.Optional;

@AutoInstance
public class BlockSmelter {
	public BlockSmelter() {
		OnLoot.listen( this::replaceWithSmeltedLoot )
			.priority( Priority.HIGH )
			.addCondition( OnLoot.hasBlockState() )
			.addCondition( OnLoot.hasTool() )
			.addCondition( Condition.isServer() )
			.addCondition( Condition.predicate( data->data.entity instanceof Player player && !player.isCrouching() ) );
	}

	private void replaceWithSmeltedLoot( OnLoot.Data data ) {
		OnBlockSmeltCheck.Data extraData = OnBlockSmeltCheck.dispatch( data.tool, data.blockState, ( Player )data.entity );
		if( !extraData.shouldSmelt )
			return;

		List< ItemStack > generatedLoot = data.generatedLoot;
		ServerLevel level = data.getServerLevel();
		Vec3 position = data.origin;
		RecipeManager recipeManager = level.getRecipeManager();

		int amountOfSmeltedItems = 0;
		ObjectArrayList< ItemStack > smeltedLoot = new ObjectArrayList<>();
		for( ItemStack itemStack : generatedLoot ) {
			smeltedLoot.add( getSmeltedItemStack( itemStack, level ) );
			Optional< SmeltingRecipe > recipe = recipeManager.getRecipeFor( RecipeType.SMELTING, new SimpleContainer( itemStack ), level );
			if( recipe.isEmpty() )
				continue;

			int experience = Random.roundRandomly( recipe.get().getExperience() * itemStack.getCount() );
			if( experience > 0 ) {
				EntityHelper.spawnExperience( data.getLevel(), position, experience );
			}

			amountOfSmeltedItems = amountOfSmeltedItems + itemStack.getCount();
		}

		if( amountOfSmeltedItems > 0 ) {
			ParticleHandler.SMELT.spawn( level, position, amountOfSmeltedItems + Random.nextInt( 4, 7 ) );
		}

		generatedLoot.clear();
		generatedLoot.addAll( smeltedLoot );
	}

	private ItemStack smeltIfPossible( ItemStack itemStack, ServerLevel level ) {
		return level.getRecipeManager()
			.getRecipeFor( RecipeType.SMELTING, new SimpleContainer( itemStack ), level )
			.map( recipe->recipe.getResultItem( level.registryAccess() ) )
			.filter( i->!i.isEmpty() )
			.map( i->ItemHandlerHelper.copyStackWithSize( i, itemStack.getCount() * i.getCount() ) )
			.orElse( itemStack );
	}

	private ItemStack getSmeltedItemStack( ItemStack itemStackToSmelt, ServerLevel level ) {
		ItemStack smeltedItemStack = smeltIfPossible( itemStackToSmelt, level );
		if( smeltedItemStack.getCount() != itemStackToSmelt.getCount() ) {
			smeltedItemStack.setCount( itemStackToSmelt.getCount() );
		}

		return smeltedItemStack;
	}
}
