package com.mlib.events;

import com.mlib.annotations.AutoInstance;
import com.mlib.gamemodifiers.GameModifier;
import com.mlib.gamemodifiers.contexts.OnLoot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.event.IModBusEvent;

import java.util.List;

/** Event called when player destroys any block related to harvesting. (for example wheat, carrots etc.) */
public class HarvestCropEvent extends PlayerEvent implements IModBusEvent {
	public final List< ItemStack > generatedLoot;
	public final CropBlock crops;
	public final BlockState blockState;
	public final ItemStack tool;
	public final Vec3 origin;

	public HarvestCropEvent( Player player, List< ItemStack > generatedLoot, CropBlock crops, BlockState blockState,
		ItemStack tool, Vec3 origin
	) {
		super( player );

		this.generatedLoot = generatedLoot;
		this.crops = crops;
		this.blockState = blockState;
		this.tool = tool;
		this.origin = origin;
	}

	@AutoInstance
	public static class Dispatcher extends GameModifier {
		public Dispatcher() {
			new OnLoot.Context( this::dispatchCropEvent )
				.addCondition( OnLoot.HAS_BLOCK_STATE.and( data->data.blockState.getBlock() instanceof CropBlock ) )
				.addCondition( OnLoot.HAS_ENTITY.and( data->data.entity instanceof Player ) )
				.addCondition( OnLoot.HAS_TOOL )
				.addCondition( OnLoot.HAS_ORIGIN )
				.insertTo( this );
		}

		private void dispatchCropEvent( OnLoot.Data data ) {
			MinecraftForge.EVENT_BUS.post( new HarvestCropEvent( ( Player )data.entity, data.generatedLoot, ( CropBlock )data.blockState.getBlock(), data.blockState, data.tool, data.origin ) );
		}
	}
}
