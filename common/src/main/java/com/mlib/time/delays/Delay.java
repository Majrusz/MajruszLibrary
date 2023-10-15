package com.mlib.time.delays;

import java.util.function.Consumer;

public class Delay implements IDelayedExecution {
	private final Consumer< Delay > callback;
	private int ticksLeft;

	public Delay( Consumer< Delay > callback, int ticks ) {
		this.callback = callback;
		this.ticksLeft = ticks;
	}

	@Override
	public void tick() {
		--this.ticksLeft;
	}

	@Override
	public void finish() {
		this.callback.accept( this );
	}

	@Override
	public boolean isFinished() {
		return this.ticksLeft == 0;
	}
}