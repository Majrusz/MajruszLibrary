package com.majruszlibrary.events;

import com.majruszlibrary.events.base.Event;
import com.majruszlibrary.events.base.Events;
import com.majruszlibrary.events.type.IEntityEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Consumer;

public class OnEntityTicked implements IEntityEvent {
	public final LivingEntity entity;

	public static Event< OnEntityTicked > listen( Consumer< OnEntityTicked > consumer ) {
		return Events.get( OnEntityTicked.class ).add( consumer );
	}

	public OnEntityTicked( LivingEntity entity ) {
		this.entity = entity;
	}

	@Override
	public Entity getEntity() {
		return this.entity;
	}
}
