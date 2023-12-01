package com.majruszlibrary.events;

import com.majruszlibrary.events.base.Event;
import com.majruszlibrary.events.base.Events;
import com.majruszlibrary.events.type.IEntityEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Consumer;

public class OnItemSwingDurationGet implements IEntityEvent {
	public final LivingEntity entity;
	public final int original;
	public int duration;

	public static Event< OnItemSwingDurationGet > listen( Consumer< OnItemSwingDurationGet > consumer ) {
		return Events.get( OnItemSwingDurationGet.class ).add( consumer );
	}

	public OnItemSwingDurationGet( LivingEntity entity, int duration ) {
		this.entity = entity;
		this.original = duration;
		this.duration = duration;
	}

	@Override
	public Entity getEntity() {
		return this.entity;
	}

	public int getSwingDuration() {
		return Math.max( this.duration, 2 );
	}
}
