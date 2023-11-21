package com.majruszlibrary.events;

import com.majruszlibrary.events.base.Events;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber( value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD )
public class OnGuiOverlaysRegisteredForge {
	@SubscribeEvent
	public static void registerGui( RegisterGuiOverlaysEvent event ) {
		for( Pair< String, OnGuiOverlaysRegistered.Renderer > pair : Events.dispatch( new OnGuiOverlaysRegistered() ).getRenderers() ) {
			event.registerBelowAll( pair.getFirst(), new IGuiOverlay() {
				@Override
				public void render( ForgeGui gui, GuiGraphics graphics, float partialTick, int screenWidth, int screenHeight ) {
					pair.getSecond().render( graphics, partialTick, screenWidth, screenHeight );
				}
			} );
		}
	}
}
