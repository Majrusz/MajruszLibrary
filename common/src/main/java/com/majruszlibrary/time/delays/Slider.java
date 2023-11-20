package com.majruszlibrary.time.delays;

import net.minecraft.util.Mth;

import java.util.function.Consumer;

public class Slider implements IDelayedExecution {
	private final Consumer< Slider > callback;
	private final int ticksTotal;
	private int ticksLeft;

	public Slider( Consumer< Slider > callback, int ticks ) {
		this.callback = callback;
		this.ticksTotal = ticks;
		this.ticksLeft = ticks;
	}

	@Override
	public void start() {
		this.callback.accept( this );
	}

	@Override
	public void tick() {
		--this.ticksLeft;
		this.callback.accept( this );
	}

	@Override
	public boolean isFinished() {
		return this.ticksLeft == 0;
	}

	public float getRatio() {
		return 1.0f - this.getRatioLeft();
	}

	public float getRatioLeft() {
		return Mth.clamp( 1.0f * this.ticksLeft / this.ticksTotal, 0.0f, 1.0f );
	}

	public int getTicksLeft() {
		return this.ticksLeft;
	}

	public int getTicksTotal() {
		return this.ticksTotal;
	}
}