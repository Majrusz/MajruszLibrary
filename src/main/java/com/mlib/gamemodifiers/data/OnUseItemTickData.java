package com.mlib.gamemodifiers.data;

import com.mlib.gamemodifiers.ContextData;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;

public class OnUseItemTickData extends ContextData {
	public final LivingEntityUseItemEvent.Tick event;
	public final LivingEntity entity;
	public final ItemStack itemStack;
	public final int duration;

	public OnUseItemTickData( LivingEntityUseItemEvent.Tick event ) {
		super( null );
		this.event = event;
		this.entity = event.getEntityLiving();
		this.itemStack = event.getItem();
		this.duration = event.getDuration();
	}
}
