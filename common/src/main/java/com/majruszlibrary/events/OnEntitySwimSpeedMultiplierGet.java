package com.majruszlibrary.events;

import com.majruszlibrary.events.base.Event;
import com.majruszlibrary.events.base.Events;
import com.majruszlibrary.events.type.IEntityEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Consumer;

public class OnEntitySwimSpeedMultiplierGet implements IEntityEvent {
	public final LivingEntity entity;
	public final float original;
	public float multiplier;

	public static Event< OnEntitySwimSpeedMultiplierGet > listen( Consumer< OnEntitySwimSpeedMultiplierGet > consumer ) {
		return Events.get( OnEntitySwimSpeedMultiplierGet.class ).add( consumer );
	}

	public OnEntitySwimSpeedMultiplierGet( LivingEntity entity, float multiplier ) {
		this.entity = entity;
		this.original = multiplier;
		this.multiplier = multiplier;
	}

	@Override
	public Entity getEntity() {
		return this.entity;
	}

	public float getMultiplier() {
		return Math.max( 0.0f, this.multiplier );
	}
}
