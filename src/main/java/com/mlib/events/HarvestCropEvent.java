package com.mlib.events;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
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

	public HarvestCropEvent( Player player, List< ItemStack > generatedLoot, CropBlock crops, BlockState blockState, ItemStack tool, Vec3 origin ) {
		super( player );

		this.generatedLoot = generatedLoot;
		this.crops = crops;
		this.blockState = blockState;
		this.tool = tool;
		this.origin = origin;
	}
}
