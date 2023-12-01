package com.majruszlibrary.events;

import com.majruszlibrary.events.base.Event;
import com.majruszlibrary.events.base.Events;
import com.majruszlibrary.events.type.IEntityEvent;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;

import java.util.function.Consumer;

public class OnItemBrushed implements IEntityEvent {
	public final Player player;
	public final ResourceLocation location;
	public final ItemStack itemStack;
	public final Direction direction;
	public final BrushableBlockEntity blockEntity;

	public static Event< OnItemBrushed > listen( Consumer< OnItemBrushed > consumer ) {
		return Events.get( OnItemBrushed.class ).add( consumer );
	}

	public OnItemBrushed( Player player, ResourceLocation location, ItemStack itemStack, Direction direction, BrushableBlockEntity blockEntity ) {
		this.player = player;
		this.location = location;
		this.itemStack = itemStack;
		this.direction = direction;
		this.blockEntity = blockEntity;
	}

	@Override
	public Entity getEntity() {
		return this.player;
	}
}