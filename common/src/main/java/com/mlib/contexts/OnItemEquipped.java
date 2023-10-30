package com.mlib.contexts;

import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;
import com.mlib.contexts.data.IEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

public class OnItemEquipped implements IEntityData {
	public final LivingEntity entity;
	public final EquipmentSlot slot;
	public final ItemStack from;
	public final ItemStack to;

	public static Context< OnItemEquipped > listen( Consumer< OnItemEquipped > consumer ) {
		return Contexts.get( OnItemEquipped.class ).add( consumer );
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
