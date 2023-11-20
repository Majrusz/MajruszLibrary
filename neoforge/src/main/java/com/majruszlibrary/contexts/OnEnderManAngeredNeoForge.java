package com.majruszlibrary.contexts;

import com.majruszlibrary.contexts.base.Contexts;
import net.minecraftforge.event.entity.living.EnderManAngerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class OnEnderManAngeredNeoForge {
	@SubscribeEvent
	public static void onAnger( EnderManAngerEvent event ) {
		if( Contexts.dispatch( new OnEnderManAngered( event.getEntity(), event.getPlayer() ) ).isAngerCancelled() ) {
			event.setCanceled( true );
		}
	}
}
