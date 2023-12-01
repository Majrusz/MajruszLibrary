package com.majruszlibrary.events;

import com.majruszlibrary.events.base.Events;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class OnBreakSpeedGetForge {
	@SubscribeEvent
	public static void onBreakSpeed( PlayerEvent.BreakSpeed event ) {
		event.setNewSpeed( Events.dispatch( new OnBreakSpeedGet( event.getEntity(), event.getState(), event.getNewSpeed() ) ).speed );
	}
}
