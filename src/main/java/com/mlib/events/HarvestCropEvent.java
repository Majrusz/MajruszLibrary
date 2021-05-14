package com.mlib.events;

import net.minecraft.block.BlockState;
import net.minecraft.block.CropsBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.event.lifecycle.IModBusEvent;

import java.util.List;

/** Event called when player destroys any block related to harvesting. (for example wheat, carrots etc.)*/
public class HarvestCropEvent extends PlayerEvent implements IModBusEvent {
	public final List< ItemStack > generatedLoot;
	public final CropsBlock crops;
	public final BlockState blockState;
	public final ItemStack tool;
	public final Vector3d origin;

	public HarvestCropEvent( PlayerEntity player, List< ItemStack > generatedLoot, CropsBlock crops, BlockState blockState, ItemStack tool, Vector3d origin ) {
		super( player );

		this.generatedLoot = generatedLoot;
		this.crops = crops;
		this.blockState = blockState;
		this.tool = tool;
		this.origin = origin;
	}
}
