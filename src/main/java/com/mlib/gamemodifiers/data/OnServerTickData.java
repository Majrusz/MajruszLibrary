package com.mlib.gamemodifiers.data;

import com.mlib.gamemodifiers.ContextData;
import net.minecraftforge.event.TickEvent;

public class OnServerTickData extends ContextData.Event< TickEvent.ServerTickEvent > {
	public OnServerTickData( TickEvent.ServerTickEvent event ) {
		super( null, event );
	}
}