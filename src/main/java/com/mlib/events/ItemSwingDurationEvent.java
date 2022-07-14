package com.mlib.events;

import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.IModBusEvent;

public class ItemSwingDurationEvent extends Event implements IModBusEvent {
	public final LivingEntity entity;
	public final int swingDuration;
	public float extraDuration = 0.0f;

	public ItemSwingDurationEvent( LivingEntity entity, int swingDuration ) {
		this.entity = entity;
		this.swingDuration = swingDuration;
	}
}
