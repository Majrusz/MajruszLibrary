package com.mlib.time;

import net.minecraft.util.Mth;

import java.util.function.Consumer;

public class Slider implements IAnimation {
	final Consumer< Slider > onTick;
	final int ticksTotal;
	int ticksLeft;

	Slider( Consumer< Slider > onTick, int ticks ) {
		this.onTick = onTick;
		this.ticksTotal = ticks;
		this.ticksLeft = ticks;
	}

	@Override
	public void onStart() {
		this.onTick.accept( this );
	}

	@Override
	public void onTick() {
		--this.ticksLeft;
		this.onTick.accept( this );
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