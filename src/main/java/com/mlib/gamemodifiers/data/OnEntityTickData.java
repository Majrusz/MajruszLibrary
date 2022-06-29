package com.mlib.gamemodifiers.data;

import com.mlib.gamemodifiers.ContextData;
import net.minecraftforge.event.entity.living.LivingEvent;

public class OnEntityTickData extends ContextData {
	public final LivingEvent.LivingUpdateEvent event;

	public OnEntityTickData( LivingEvent.LivingUpdateEvent event ) {
		super( event.getEntityLiving() );
		this.event = event;
	}
}
