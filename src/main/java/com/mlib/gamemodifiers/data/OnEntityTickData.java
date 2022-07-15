package com.mlib.gamemodifiers.data;

import com.mlib.gamemodifiers.ContextData;
import net.minecraftforge.event.entity.living.LivingEvent;

public class OnEntityTickData extends ContextData.Event< LivingEvent.LivingTickEvent > {
	public OnEntityTickData( LivingEvent.LivingTickEvent event ) {
		super( event.getEntity(), event );
	}
}
