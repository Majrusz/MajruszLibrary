package com.majruszlibrary.events;

import com.majruszlibrary.events.base.Events;
import net.minecraftforge.event.entity.living.EnderManAngerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class OnEnderManAngeredForge {
	@SubscribeEvent
	public static void onAnger( EnderManAngerEvent event ) {
		if( Events.dispatch( new OnEnderManAngered( event.getEntity(), event.getPlayer() ) ).isAngerCancelled() ) {
			event.setCanceled( true );
		}
	}
}
