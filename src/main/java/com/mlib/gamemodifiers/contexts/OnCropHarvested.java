package com.mlib.gamemodifiers.contexts;

import com.mlib.annotations.AutoInstance;
import com.mlib.gamemodifiers.Condition;
import com.mlib.gamemodifiers.ContextBase;
import com.mlib.gamemodifiers.Contexts;
import com.mlib.gamemodifiers.GameModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.Consumer;

public class OnCropHarvested {
	public static ContextBase< Data > listen( Consumer< Data > consumer ) {
		return Contexts.get( Data.class ).add( consumer );
	}

	public static class Data {
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
	}

	@AutoInstance
	public static class Dispatcher extends GameModifier {
		public Dispatcher() {
			OnLoot.listen( this::dispatchCropEvent )
				.addCondition( OnLoot.hasBlockState() )
				.addCondition( OnLoot.hasEntity() )
				.addCondition( OnLoot.hasTool() )
				.addCondition( OnLoot.hasOrigin() )
				.addCondition( Condition.predicate( data->data.blockState.getBlock() instanceof CropBlock ) )
				.addCondition( Condition.predicate( data->data.entity instanceof Player ) )
				.insertTo( this );
		}

		private void dispatchCropEvent( OnLoot.Data data ) {
			Contexts.get( Data.class )
				.dispatch( new Data( ( Player )data.entity, data.generatedLoot, ( CropBlock )data.blockState.getBlock(), data.blockState, data.tool, data.origin ) );
		}
	}
}