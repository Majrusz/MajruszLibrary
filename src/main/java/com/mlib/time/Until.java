package com.mlib.time;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class Until implements ISuspendedExecution {
	final Consumer< Until > onEnd;
	final Supplier< Boolean > isReady;

	Until( Consumer< Until > onEnd, Supplier< Boolean > isReady ) {
		this.onEnd = onEnd;
		this.isReady = isReady;
	}

	@Override
	public void onEnd() {
		this.onEnd.accept( this );
	}

	@Override
	public boolean isFinished() {
		return this.isReady.get();
	}
}