package com.majruszlibrary.events;

import com.majruszlibrary.annotation.Dist;
import com.majruszlibrary.annotation.OnlyIn;
import com.majruszlibrary.events.base.Event;
import com.majruszlibrary.events.base.Events;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.gui.GuiGraphics;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@OnlyIn( Dist.CLIENT )
public class OnGuiOverlaysRegistered {
	private final List< Pair< String, Renderer > > renderers = new ArrayList<>();

	public static Event< OnGuiOverlaysRegistered > listen( Consumer< OnGuiOverlaysRegistered > consumer ) {
		return Events.get( OnGuiOverlaysRegistered.class ).add( consumer );
	}

	public void register( String id, Renderer renderer ) {
		this.renderers.add( Pair.of( id, renderer ) );
	}

	public List< Pair< String, Renderer > > getRenderers() {
		return this.renderers;
	}

	@FunctionalInterface
	@OnlyIn( Dist.CLIENT )
	public interface Renderer {
		void render( GuiGraphics graphics, float partialTick, int screenWidth, int screenHeight );
	}
}
