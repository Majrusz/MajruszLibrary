package com.mlib.loot_modifiers;

import com.google.common.base.Suppliers;
import com.google.gson.JsonObject;
import com.mlib.events.HarvestCropEvent;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

/** Loot modifier for items gathered from harvesting. (wheat, carrots etc.) */
public class HarvestCrop extends LootModifier {
	public static final Supplier< Codec< HarvestCrop > > CODEC = Suppliers.memoize( ()->RecordCodecBuilder.create( inst->codecStart( inst ).apply( inst, HarvestCrop::new ) ) );

	public HarvestCrop( LootItemCondition[] conditionsIn ) {
		super( conditionsIn );
	}

	@Nonnull
	@Override
	public ObjectArrayList< ItemStack > doApply( ObjectArrayList< ItemStack > generatedLoot, LootContext context ) {
		BlockState blockState = LootHelper.getParameter( context, LootContextParams.BLOCK_STATE );
		if( blockState == null || !( blockState.getBlock() instanceof CropBlock ) )
			return generatedLoot;

		CropBlock crops = ( CropBlock )blockState.getBlock();
		Entity entity = LootHelper.getParameter( context, LootContextParams.THIS_ENTITY );
		ItemStack tool = LootHelper.getParameter( context, LootContextParams.TOOL );
		Vec3 origin = LootHelper.getParameter( context, LootContextParams.ORIGIN );
		if( origin == null || !( entity instanceof Player ) )
			return generatedLoot;

		MinecraftForge.EVENT_BUS.post( new HarvestCropEvent( ( Player )entity, generatedLoot, crops, blockState, tool, origin ) );
		return generatedLoot;
	}

	@Override
	public Codec< ? extends IGlobalLootModifier > codec() {
		return CODEC.get();
	}
}
