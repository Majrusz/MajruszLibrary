package com.mlib.events;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.IModBusEvent;

public class ItemSwingDurationEvent extends Event implements IModBusEvent {
	public final LivingEntity entity;
	public final int swingDuration;
	public int extraDuration = 0;

	public ItemSwingDurationEvent( LivingEntity entity, int swingDuration ) {
		this.entity = entity;
		this.swingDuration = swingDuration;
	}

	public int getTotalSwingDuration() {
		return Mth.clamp( this.swingDuration + this.extraDuration, 1, 100 );
	}
}
