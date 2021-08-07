package com.mlib.loot_modifiers;

import com.google.gson.JsonObject;
import com.mlib.LevelHelper;
import com.mlib.MajruszLibrary;
import com.mlib.Random;
import com.mlib.entities.EntityHelper;
import com.mlib.particles.ParticleHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** Smelts blocks after destroying them. */
public class SmeltingItems extends LootModifier {
	public static final List< Register > registerList = new ArrayList<>();

	protected SmeltingItems( LootItemCondition[] conditions ) {
		super( conditions );
	}

	@Nonnull
	@Override
	protected List< ItemStack > doApply( List< ItemStack > generatedLoot, LootContext context ) {
		ItemStack tool = LootHelper.getParameter( context, LootContextParams.TOOL );
		Player player = EntityHelper.getPlayerFromEntity( LootHelper.getParameter( context, LootContextParams.THIS_ENTITY ) );
		BlockState blockState = LootHelper.getParameter( context, LootContextParams.BLOCK_STATE );
		ServerLevel level = LevelHelper.getServerLevelFromEntity( player );
		if( blockState == null || level == null || player.isCrouching() || !shouldBeExecuted( level, player, tool, blockState ) )
			return generatedLoot;

		return getSmeltedLoot( generatedLoot, context.getLevel(), LootHelper.getParameter( context, LootContextParams.ORIGIN ) );
	}

	/** Returns whether blocks should be smelted if possible. */
	protected static boolean shouldBeExecuted( ServerLevel level, Player player, @Nullable ItemStack tool, BlockState blockState ) {
		for( Register register : registerList )
			if( register.shouldBeExecuted( level, player, tool, blockState ) )
				return true;

		return false;
	}

	/** Smelts given item stack if possible. */
	protected ItemStack smeltIfPossible( ItemStack itemStack, ServerLevel level ) {
		return level.getRecipeManager()
			.getRecipeFor( RecipeType.SMELTING, new SimpleContainer( itemStack ), level )
			.map( SmeltingRecipe::getResultItem )
			.filter( i->!i.isEmpty() )
			.map( i->ItemHandlerHelper.copyStackWithSize( i, itemStack.getCount() * i.getCount() ) )
			.orElse( itemStack );
	}

	/**
	 Calculates random experience for smelted items.
	 For example if smelting recipe gives 0.4 XP and it has smelted 6 items.
	 Then this gives player 2 XP points and has 40% (0.4) chance for another 1 XP point. (0.4 XP * 6 items = 2.4 XP)
	 */
	protected int calculateRandomExperienceForRecipe( SmeltingRecipe recipe, int smeltedItems ) {
		return Random.randomizeExperience( recipe.getExperience() * smeltedItems );
	}

	/** Returns smelted item stack. */
	protected ItemStack getSmeltedItemStack( ItemStack itemStackToSmelt, ServerLevel level ) {
		ItemStack smeltedItemStack = smeltIfPossible( itemStackToSmelt, level );
		if( smeltedItemStack.getCount() != itemStackToSmelt.getCount() )
			smeltedItemStack.setCount( itemStackToSmelt.getCount() );

		return smeltedItemStack;
	}

	/** Returns smelted generated loot. */
	protected List< ItemStack > getSmeltedLoot( List< ItemStack > generatedLoot, ServerLevel level, Vec3 position ) {
		RecipeManager recipeManager = level.getRecipeManager();

		int amountOfSmeltedItems = 0;
		ArrayList< ItemStack > smeltedLoot = new ArrayList<>();
		for( ItemStack itemStack : generatedLoot ) {
			smeltedLoot.add( getSmeltedItemStack( itemStack, level ) );
			Optional< SmeltingRecipe > recipe = recipeManager.getRecipeFor( RecipeType.SMELTING, new SimpleContainer( itemStack ), level );
			if( recipe.isEmpty() )
				continue;

			int experience = calculateRandomExperienceForRecipe( recipe.get(), itemStack.getCount() );
			if( experience > 0 )
				level.addFreshEntity( new ExperienceOrb( level, position.x, position.y, position.z, experience ) );

			amountOfSmeltedItems = amountOfSmeltedItems + itemStack.getCount();
		}

		if( amountOfSmeltedItems > 0 )
			ParticleHelper.spawnSmeltParticles( level, position, 3 + amountOfSmeltedItems + MajruszLibrary.RANDOM.nextInt( 4 ) );

		return smeltedLoot;
	}

	public static class Serializer extends GlobalLootModifierSerializer< SmeltingItems > {
		@Override
		public SmeltingItems read( ResourceLocation name, JsonObject object, LootItemCondition[] conditionsIn ) {
			return new SmeltingItems( conditionsIn );
		}

		@Override
		public JsonObject write( SmeltingItems instance ) {
			return null;
		}
	}

	@FunctionalInterface
	public interface Register {
		boolean shouldBeExecuted( ServerLevel level, Player player, @Nullable ItemStack tool, BlockState blockState );
	}
}
