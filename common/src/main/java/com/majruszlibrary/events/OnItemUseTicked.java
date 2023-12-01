package com.majruszlibrary.events;

import com.majruszlibrary.events.base.Event;
import com.majruszlibrary.events.base.Events;
import com.majruszlibrary.events.type.IEntityEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

public class OnItemUseTicked implements IEntityEvent {
	public final LivingEntity entity;
	public final ItemStack itemStack;
	public final int maxDuration;
	public final int original;
	public int duration;

	public static Event< OnItemUseTicked > listen( Consumer< OnItemUseTicked > consumer ) {
		return Events.get( OnItemUseTicked.class ).add( consumer );
	}

	public OnItemUseTicked( LivingEntity entity, ItemStack itemStack, int maxDuration, int duration ) {
		this.entity = entity;
		this.itemStack = itemStack;
		this.maxDuration = maxDuration;
		this.original = duration;
		this.duration = duration;
	}

	@Override
	public Entity getEntity() {
		return this.entity;
	}

	public int getDuration() {
		return Mth.clamp( this.duration, 0, this.maxDuration );
	}
}
