package com.mlib.contexts;

import com.mlib.annotation.AutoInstance;
import com.mlib.contexts.base.Condition;
import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;
import com.mlib.contexts.data.IEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.Consumer;

public class OnCropHarvested implements IEntityData {
	public final Player player;
	public final List< ItemStack > generatedLoot;
	public final CropBlock crops;
	public final BlockState blockState;
	public final ItemStack tool;
	public final Vec3 origin;

	public static Context< OnCropHarvested > listen( Consumer< OnCropHarvested > consumer ) {
		return Contexts.get( OnCropHarvested.class ).add( consumer );
	}

	public OnCropHarvested( Player player, List< ItemStack > generatedLoot, CropBlock crops, BlockState blockState, ItemStack tool, Vec3 origin ) {
		this.player = player;
		this.generatedLoot = generatedLoot;
		this.crops = crops;
		this.blockState = blockState;
		this.tool = tool;
		this.origin = origin;
	}

	@Override
	public Entity getEntity() {
		return this.player;
	}

	@AutoInstance
	public static class Dispatcher {
		public Dispatcher() {
			OnLootGenerated.listen( this::dispatchCropEvent )
				.addCondition( Condition.predicate( OnLootGenerated::hasBlockState ) )
				.addCondition( Condition.predicate( OnLootGenerated::hasEntity ) )
				.addCondition( Condition.predicate( OnLootGenerated::hasTool ) )
				.addCondition( Condition.predicate( OnLootGenerated::hasOrigin ) )
				.addCondition( Condition.predicate( data->data.blockState.getBlock() instanceof CropBlock ) )
				.addCondition( Condition.predicate( data->data.entity instanceof Player ) );
		}

		private void dispatchCropEvent( OnLootGenerated data ) {
			Contexts.dispatch( new OnCropHarvested( ( Player )data.entity, data.generatedLoot, ( CropBlock )data.blockState.getBlock(), data.blockState, data.tool, data.origin ) );
		}
	}
}