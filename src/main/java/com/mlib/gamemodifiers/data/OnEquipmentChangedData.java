package com.mlib.gamemodifiers.data;

import com.mlib.gamemodifiers.ContextData;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;

public class OnEquipmentChangedData extends ContextData {
	public final LivingEquipmentChangeEvent event;
	public final LivingEntity entity;

	public OnEquipmentChangedData( LivingEquipmentChangeEvent event ) {
		super( null );
		this.event = event;
		this.entity = event.getEntityLiving();
	}
}
