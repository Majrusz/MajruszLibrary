package com.mlib.features;

import com.mlib.Random;
import com.mlib.effects.ParticleHandler;
import com.mlib.events.BlockSmeltCheckEvent;
import com.mlib.gamemodifiers.GameModifier;
import com.mlib.gamemodifiers.contexts.OnLoot;
import com.mlib.gamemodifiers.parameters.ContextParameters;
import com.mlib.gamemodifiers.parameters.Priority;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.List;
import java.util.Optional;

public class BlockSmelter extends GameModifier {
	public BlockSmelter() {
		super( "BlockSmelter", "" );

		OnLoot.Context onLoot = new OnLoot.Context( this::replaceWithSmeltedLoot, new ContextParameters( Priority.HIGH, "", "" ) );
		onLoot.addCondition( data->data.blockState != null )
			.addCondition( data->data.level != null )
			.addCondition( data->data.entity instanceof Player player && !player.isCrouching() )
			.addCondition( this::checkEventListeners );

		this.addContext( onLoot );
	}

	private boolean checkEventListeners( OnLoot.Data data ) {
		BlockSmeltCheckEvent event = new BlockSmeltCheckEvent( data.event );
		MinecraftForge.EVENT_BUS.post( event );

		return event.shouldSmelt;
	}

	private void replaceWithSmeltedLoot( OnLoot.Data data ) {
		List< ItemStack > generatedLoot = data.generatedLoot;
		ServerLevel level = data.level;
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
			if( experience > 0 )
				level.addFreshEntity( new ExperienceOrb( level, position.x, position.y, position.z, experience ) );

			amountOfSmeltedItems = amountOfSmeltedItems + itemStack.getCount();
		}

		if( amountOfSmeltedItems > 0 )
			ParticleHandler.SMELT.spawn( level, position, amountOfSmeltedItems + Random.nextInt( 4, 7 ) );

		generatedLoot.clear();
		generatedLoot.addAll( smeltedLoot );
	}

	private ItemStack smeltIfPossible( ItemStack itemStack, ServerLevel level ) {
		return level.getRecipeManager()
			.getRecipeFor( RecipeType.SMELTING, new SimpleContainer( itemStack ), level )
			.map( SmeltingRecipe::getResultItem )
			.filter( i->!i.isEmpty() )
			.map( i->ItemHandlerHelper.copyStackWithSize( i, itemStack.getCount() * i.getCount() ) )
			.orElse( itemStack );
	}

	private ItemStack getSmeltedItemStack( ItemStack itemStackToSmelt, ServerLevel level ) {
		ItemStack smeltedItemStack = smeltIfPossible( itemStackToSmelt, level );
		if( smeltedItemStack.getCount() != itemStackToSmelt.getCount() )
			smeltedItemStack.setCount( itemStackToSmelt.getCount() );

		return smeltedItemStack;
	}
}
