package com.majruszlibrary.events;

import com.majruszlibrary.events.base.Event;
import com.majruszlibrary.events.base.Events;
import com.majruszlibrary.events.type.IEntityEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

public class OnItemEquipped implements IEntityEvent {
	public final LivingEntity entity;
	public final EquipmentSlot slot;
	public final ItemStack from;
	public final ItemStack to;

	public static Event< OnItemEquipped > listen( Consumer< OnItemEquipped > consumer ) {
		return Events.get( OnItemEquipped.class ).add( consumer );
	}

	public OnItemEquipped( LivingEntity entity, EquipmentSlot slot, ItemStack from, ItemStack to ) {
		this.entity = entity;
		this.slot = slot;
		this.from = from;
		this.to = to;
	}

	@Override
	public Entity getEntity() {
		return this.entity;
	}
}
