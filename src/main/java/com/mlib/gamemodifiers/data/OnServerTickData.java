package com.mlib.gamemodifiers.data;

import com.mlib.gamemodifiers.ContextData;
import net.minecraftforge.event.TickEvent;

public class OnServerTickData extends ContextData {
	public final TickEvent.ServerTickEvent event;

	public OnServerTickData( TickEvent.ServerTickEvent event ) {
		super( null );
		this.event = event;
	}
}