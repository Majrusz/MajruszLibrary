package com.majruszlibrary.events;

import com.majruszlibrary.events.base.Event;
import com.majruszlibrary.events.base.Events;
import com.majruszlibrary.events.type.ICancellableEvent;
import com.majruszlibrary.events.type.IEntityEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

public class OnChorusFruitEaten implements ICancellableEvent, IEntityEvent {
	public final ItemStack itemStack;
	public final Level level;
	public final LivingEntity entity;
	private boolean isTeleportCancelled = false;

	public static Event< OnChorusFruitEaten > listen( Consumer< OnChorusFruitEaten > consumer ) {
		return Events.get( OnChorusFruitEaten.class ).add( consumer );
	}

	public OnChorusFruitEaten( ItemStack itemStack, Level level, LivingEntity entity ) {
		this.itemStack = itemStack;
		this.level = level;
		this.entity = entity;
	}

	@Override
	public boolean isExecutionStopped() {
		return this.isTeleportCancelled();
	}

	@Override
	public Entity getEntity() {
		return this.entity;
	}

	public void cancelTeleport() {
		this.isTeleportCancelled = true;
	}

	public boolean isTeleportCancelled() {
		return this.isTeleportCancelled;
	}
}
