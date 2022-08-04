package com.mlib.gamemodifiers.data;

import com.mlib.gamemodifiers.ContextData;
import net.minecraftforge.event.entity.living.LivingEvent;

public class OnEntityTickData extends ContextData.Event< LivingEvent.LivingUpdateEvent > {
	public OnEntityTickData( LivingEvent.LivingUpdateEvent event ) {
		super( event.getEntityLiving(), event );
	}
}
