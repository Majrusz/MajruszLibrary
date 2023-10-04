package com.mlib.contexts;

import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;
import com.mlib.contexts.data.IEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

public class OnEntityEquipmentChanged implements IEntityData {
	public final LivingEntity entity;
	public final ItemStack oldItemStack;
	public final ItemStack newItemStack;

	public static Context< OnEntityEquipmentChanged > listen( Consumer< OnEntityEquipmentChanged > consumer ) {
		return Contexts.get( OnEntityEquipmentChanged.class ).add( consumer );
	}

	public OnEntityEquipmentChanged( LivingEntity entity, ItemStack oldItemStack, ItemStack newItemStack ) {
		this.entity = entity;
		this.oldItemStack = oldItemStack;
		this.newItemStack = newItemStack;
	}

	@Override
	public Entity getEntity() {
		return this.entity;
	}
}
