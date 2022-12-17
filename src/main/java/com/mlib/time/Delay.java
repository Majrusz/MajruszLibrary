package com.mlib.time;

import java.util.function.Consumer;

public class Delay implements ISuspendedExecution {
	final Consumer< Delay > onEnd;
	int ticksLeft;

	Delay( Consumer< Delay > onEnd, int ticks ) {
		this.onEnd = onEnd;
		this.ticksLeft = ticks;
	}

	@Override
	public void onTick() {
		--this.ticksLeft;
	}

	@Override
	public void onEnd() {
		this.onEnd.accept( this );
	}

	@Override
	public boolean isFinished() {
		return this.ticksLeft == 0;
	}
}