package com.mlib.contexts;

import com.mlib.contexts.base.Contexts;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.living.EnderManAngerEvent;

@Mod.EventBusSubscriber
public class OnEnderManAngeredNeoForge {
	@SubscribeEvent
	public static void onAnger( EnderManAngerEvent event ) {
		if( Contexts.dispatch( new OnEnderManAngered( event.getEntity(), event.getPlayer() ) ).isAngerCancelled() ) {
			event.setCanceled( true );
		}
	}
}
