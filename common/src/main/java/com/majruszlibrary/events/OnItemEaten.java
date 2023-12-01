package com.majruszlibrary.events;

import com.majruszlibrary.events.base.Event;
import com.majruszlibrary.events.base.Events;
import com.majruszlibrary.events.type.IEntityEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

public class OnItemEaten implements IEntityEvent {
	public final LivingEntity entity;
	public final ItemStack itemStack;

	public static Event< OnItemEaten > listen( Consumer< OnItemEaten > consumer ) {
		return Events.get( OnItemEaten.class ).add( consumer );
	}

	public OnItemEaten( LivingEntity entity, ItemStack itemStack ) {
		this.entity = entity;
		this.itemStack = itemStack;
	}

	@Override
	public Entity getEntity() {
		return this.entity;
	}
}
