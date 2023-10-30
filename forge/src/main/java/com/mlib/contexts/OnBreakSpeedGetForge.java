package com.mlib.contexts;

import com.mlib.contexts.base.Contexts;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class OnBreakSpeedGetForge {
	@SubscribeEvent
	public static void onBreakSpeed( PlayerEvent.BreakSpeed event ) {
		event.setNewSpeed( Contexts.dispatch( new OnBreakSpeedGet( event.getEntity(), event.getState(), event.getNewSpeed() ) ).speed );
	}
}
