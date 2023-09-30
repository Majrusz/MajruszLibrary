package net.mlib.contexts;

import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class OnBreakSpeedGetForge extends OnBreakSpeedGet {
	@SubscribeEvent
	public static void onBreakSpeed( PlayerEvent.BreakSpeed event ) {
		event.setNewSpeed( OnBreakSpeedGet.dispatch( event.getEntity(), event.getState(), event.getNewSpeed() ).newSpeed );
	}
}
