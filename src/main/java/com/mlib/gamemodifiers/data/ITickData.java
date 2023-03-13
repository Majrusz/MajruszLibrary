package com.mlib.gamemodifiers.data;

import net.minecraftforge.event.TickEvent;

public interface ITickData {
	TickEvent getTickEvent();

	default TickEvent.Phase getPhase() {
		return this.getTickEvent().phase;
	}
}
