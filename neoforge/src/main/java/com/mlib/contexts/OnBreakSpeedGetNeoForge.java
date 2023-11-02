package com.mlib.contexts;

import com.mlib.contexts.base.Contexts;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@Mod.EventBusSubscriber
public class OnBreakSpeedGetNeoForge {
	@SubscribeEvent
	public static void onBreakSpeed( PlayerEvent.BreakSpeed event ) {
		event.setNewSpeed( Contexts.dispatch( new OnBreakSpeedGet( event.getEntity(), event.getState(), event.getNewSpeed() ) ).speed );
	}
}
