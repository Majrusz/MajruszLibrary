package com.mlib.time.delays;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class Until implements IDelayedExecution {
	private final Consumer< Until > callback;
	private final Predicate< Until > predicate;
	private int ticksActive = 0;

	public Until( Consumer< Until > callback, Predicate< Until > predicate ) {
		this.callback = callback;
		this.predicate = predicate;
	}

	@Override
	public void tick() {
		++this.ticksActive;
	}

	@Override
	public void finish() {
		this.callback.accept( this );
	}

	@Override
	public boolean isFinished() {
		return this.predicate.test( this );
	}

	public int getTicksActive() {
		return this.ticksActive;
	}
}