package com.majruszlibrary.events;

import com.majruszlibrary.events.base.Events;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber( value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD )
public class OnGameInitializedForge {
	@SubscribeEvent
	public static void initialize( FMLClientSetupEvent event ) {
		event.enqueueWork( ()->Events.dispatch( new OnGameInitialized() ) );
	}
}
