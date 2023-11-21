package com.majruszlibrary.events;

import com.majruszlibrary.annotation.AutoInstance;
import com.majruszlibrary.events.base.Event;
import com.majruszlibrary.events.base.Events;
import com.majruszlibrary.events.type.IEntityEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.Consumer;

public class OnCropHarvested implements IEntityEvent {
	public final Player player;
	public final List< ItemStack > generatedLoot;
	public final CropBlock crops;
	public final BlockState blockState;
	public final ItemStack tool;
	public final Vec3 origin;

	public static Event< OnCropHarvested > listen( Consumer< OnCropHarvested > consumer ) {
		return Events.get( OnCropHarvested.class ).add( consumer );
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
				.addCondition( data->data.blockState != null && data.blockState.getBlock() instanceof CropBlock )
				.addCondition( data->data.entity instanceof Player )
				.addCondition( data->data.tool != null )
				.addCondition( data->data.origin != null );
		}

		private void dispatchCropEvent( OnLootGenerated data ) {
			Events.dispatch( new OnCropHarvested( ( Player )data.entity, data.generatedLoot, ( CropBlock )data.blockState.getBlock(), data.blockState, data.tool, data.origin ) );
		}
	}
}