package com.mlib.gamemodifiers.data;

import com.mlib.gamemodifiers.ContextData;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;

public class OnEquipmentChangedData extends ContextData.Event< LivingEquipmentChangeEvent > {
	public OnEquipmentChangedData( LivingEquipmentChangeEvent event ) {
		super( event.getEntity(), event );
	}
}
