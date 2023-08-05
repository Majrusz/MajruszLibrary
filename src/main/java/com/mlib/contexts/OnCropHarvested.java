package com.mlib.contexts;

import com.mlib.annotations.AutoInstance;
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

public class OnCropHarvested {
	public static Context< Data > listen( Consumer< Data > consumer ) {
		return Contexts.get( Data.class ).add( consumer );
	}

	public static class Data implements IEntityData {
		public final Player player;
		public final List< ItemStack > generatedLoot;
		public final CropBlock crops;
		public final BlockState blockState;
		public final ItemStack tool;
		public final Vec3 origin;

		public Data( Player player, List< ItemStack > generatedLoot, CropBlock crops, BlockState blockState, ItemStack tool, Vec3 origin ) {
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
	}

	@AutoInstance
	public static class Dispatcher {
		public Dispatcher() {
			OnLoot.listen( this::dispatchCropEvent )
				.addCondition( OnLoot.hasBlockState() )
				.addCondition( OnLoot.hasEntity() )
				.addCondition( OnLoot.hasTool() )
				.addCondition( OnLoot.hasOrigin() )
				.addCondition( Condition.predicate( data->data.blockState.getBlock() instanceof CropBlock ) )
				.addCondition( Condition.predicate( data->data.entity instanceof Player ) );
		}

		private void dispatchCropEvent( OnLoot.Data data ) {
			Contexts.get( Data.class )
				.dispatch( new Data( ( Player )data.entity, data.generatedLoot, ( CropBlock )data.blockState.getBlock(), data.blockState, data.tool, data.origin ) );
		}
	}
}