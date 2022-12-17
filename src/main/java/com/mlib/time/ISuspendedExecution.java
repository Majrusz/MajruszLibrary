package com.mlib.time;

public interface ISuspendedExecution {
	default void onStart() {}

	default void onTick() {}

	default void onEnd() {}

	boolean isFinished();
}
