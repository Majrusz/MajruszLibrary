package com.mlib.loot_modifiers;

import com.google.gson.JsonObject;
import com.mlib.events.HarvestCropEvent;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropsBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

import javax.annotation.Nonnull;
import java.util.List;

/** Loot modifier for items gathered from harvesting. (wheat, carrots etc.) */
public class HarvestCrop extends LootModifier {
	public HarvestCrop( ILootCondition[] conditionsIn ) {
		super( conditionsIn );
	}

	@Nonnull
	@Override
	public List< ItemStack > doApply( List< ItemStack > generatedLoot, LootContext context ) {
		BlockState blockState = context.get( LootParameters.BLOCK_STATE );
		if( blockState == null || !( blockState.getBlock() instanceof CropsBlock ) )
			return generatedLoot;

		CropsBlock crops = ( CropsBlock )blockState.getBlock();
		Entity entity = context.get( LootParameters.THIS_ENTITY );
		ItemStack tool = context.get( LootParameters.TOOL );
		Vector3d origin = context.get( LootParameters.field_237457_g_ );
		if( origin == null || !( entity instanceof PlayerEntity ) )
			return generatedLoot;

		MinecraftForge.EVENT_BUS.post( new HarvestCropEvent( ( PlayerEntity )entity, generatedLoot, crops, blockState, tool, origin ) );
		return generatedLoot;
	}

	public static class Serializer extends GlobalLootModifierSerializer< HarvestCrop > {
		@Override
		public HarvestCrop read( ResourceLocation name, JsonObject object, ILootCondition[] conditions ) {
			return new HarvestCrop( conditions );
		}

		@Override
		public JsonObject write( HarvestCrop instance ) {
			return null;
		}
	}
}
